package com.recordbackend.Service;

import com.recordbackend.Dto.AuthResponseDto;
import com.recordbackend.Dto.LogsDto;
import com.recordbackend.Dto.UserDto;
import com.recordbackend.Dto.UserRegisterDto;
import com.recordbackend.Model.*;
import com.recordbackend.Repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private ProjectService projectService;
    @Setter
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
    public UserDto createUser(UserRegisterDto userRegisterDto) {
        // convert password with BCrypt
        String BcryptPassword = this.passwordEncoder.encode(userRegisterDto.getPassword());
        userRegisterDto.setPassword(BcryptPassword);
        // check if the email is already in use
        if (userRepository.findByEmail(userRegisterDto.getEmail()) != null) {
            throw new EntityExistsException("Email already in use");
        }
        // save the UserRegister to the database and return an UserDto
        return convertToDto(userRepository.save(convertToEntity(userRegisterDto)));
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
}
