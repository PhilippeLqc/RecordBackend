package com.recordbackend.Service;

import com.recordbackend.Dto.UserDto;
import com.recordbackend.Dto.UserRegisterDto;
import com.recordbackend.Dto.LogsDto;
import com.recordbackend.Model.Project;
import com.recordbackend.Model.User;
import com.recordbackend.Model.User_project;
import com.recordbackend.Repository.TaskRepository;
import com.recordbackend.Repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final BCryptPasswordEncoder passwordEncoder;

    // convert UserRegisterDto to User
    public User convertToEntity(UserRegisterDto userRegisterDto) {
        return User.builder()
                .username(userRegisterDto.getUsername())
                .email(userRegisterDto.getEmail())
                .password(userRegisterDto.getPassword())
                .build();
    }

    // convert UserDto to User
    public User convertToEntity(UserDto userDto) { //TODO add taskIds and projectIds by fetching from TaskService and ProjectService
        List<Project> project = userDto.getProjectIds().stream().map(projectService::findById).toList();
        // List<Task> task = userDto.getTaskIds().stream().map(taskService::findById).toList();
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                //.tasks(task)
                .user_projects(project.stream().map(p -> User_project.builder().project(p).build()).toList())
                .build();
    }

    // convert User to UserDto
    public UserDto convertToDto(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
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
}
