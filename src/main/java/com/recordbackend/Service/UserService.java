package com.recordbackend.Service;

import com.recordbackend.Dto.*;
import com.recordbackend.Model.*;
import com.recordbackend.Repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SecurityTokenService securityTokenService;
    @Setter
    @Autowired
    private EmailService emailService;

    @Setter
    @Autowired
    private ProjectService projectService;

    @Setter
    @Autowired
    private TaskService taskService;

    // convert UserRegisterDto to User
    public User convertToEntity(UserRegisterDto userRegisterDto) {
        return User.builder()
                .username(userRegisterDto.getUsername())
                .email(userRegisterDto.getEmail())
                .password(userRegisterDto.getPassword())
                .role(Role.USER)
                .build();
    }

    // convert UserDto to User
    public User convertToEntity(UserDto userDto) {
        // get all projects by projectIds in userDto and map to User_project. Return as list of User_project
        List<Project> project = userDto.getProjectIds()
                .stream()
                .map(projectService::findById)
                .toList();
        // get all tasks by taskIds in userDto and map to Task. Return as list of Task
        List<Task> task = userDto.getTaskIds()
                .stream()
                .map(taskService::getTaskById)
                .toList();
        //build User with username, email, role, tasks and user_projects
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .tasks(task)
                .user_projects(project.stream().map(p -> User_project.builder().project(p).build()).toList()) // map project to User_project and return as list of User_project
                .build();
    }

    // convert User to UserDto
    public UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(Role.USER)
                .taskIds(user.getTaskIds())
                .projectIds(user.getProjectIds())
                .build();
    }

    //convert LogsDto to UserDto
    public UserDto convertToUserDto(LogsDto logsDto) {
        User user = userRepository.findByEmailAndPassword(logsDto.getEmail(), logsDto.getPassword());
        return convertToDto(user);
    }

    //------------------------------------------------------------------------------------------------------------
    //
    // create a new user
    public void createUser(UserRegisterDto userRegisterDto) throws MessagingException {
        // convert password with BCrypt
        String BcryptPassword = this.passwordEncoder.encode(userRegisterDto.getPassword());
        userRegisterDto.setPassword(BcryptPassword);
        // check if the email is already in use
        if (userRepository.findByEmail(userRegisterDto.getEmail()) != null) {
            throw new EntityExistsException("Email already in use");
        }

        User user = userRepository.save(convertToEntity(userRegisterDto));
        System.out.println(user.getId());

        emailService.sendEmailVerification(user);
    }

    // get all users
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDto).toList();
    }

    // get user by id
    public UserDto getUserDtoById(Long id) {
        return convertToDto(getUserById(id));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    // update user by id
    public UserDto updateUserById(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        return convertToDto(userRepository.save(user));
    }

    // delete user by id
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    // get user by email
    public UserDto getUserByEmail(String email) {
        return convertToDto(userRepository.findByEmail(email));
    }

    // login user & generate tokens
    public AuthResponseDto login(LogsDto logsDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        logsDto.getEmail(),
                        logsDto.getPassword())
        );

        User user = userRepository.findByEmail(logsDto.getEmail());
        securityTokenService.deleteTokenByUser(user);

        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

        securityTokenService.saveJwtAndRefreshToken(jwt, refreshToken, user);

        return AuthResponseDto.builder()
                .token(jwt)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout() {
        User user = getAuthUser();
        securityTokenService.deleteTokenByUser(user);
    }

    public User getAuthUser() {
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getUserById(userAuth.getId());
    }

    public void activateAccount(String token) {
        SecurityToken securityToken = securityTokenService.getToken(token);

        User user = getUserById(securityToken.getUser().getId());

        user.setEnable(true);
        userRepository.save(user);
        securityTokenService.deleteToken(securityToken);
    }

    public void updatePassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        if (!resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        SecurityToken securityToken = securityTokenService.getToken(resetPasswordRequest.getToken());
        User user = securityToken.getUser();

        updatePassword(user, resetPasswordRequest.getPassword());
        securityTokenService.deleteToken(securityToken);
    }

    public AuthResponseDto changePassword(ChangePasswordRequest changePasswordRequest) {
        if (!changePasswordRequest.getPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = getAuthUser();

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
        userRepository.save(user);

        LogsDto logsDto = LogsDto.builder()
                .email(user.getEmail())
                .password(changePasswordRequest.getPassword())
                .build();

        return login(logsDto);
    }
}
