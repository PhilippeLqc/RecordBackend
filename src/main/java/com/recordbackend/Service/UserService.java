package com.recordbackend.Service;

import com.recordbackend.Dto.UserDto;
import com.recordbackend.Dto.UserRegisterDto;
import com.recordbackend.Dto.LogsDto;
import com.recordbackend.Model.Task;
import com.recordbackend.Model.User;
import com.recordbackend.Repository.TaskRepository;
import com.recordbackend.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    // convert UserRegisterDto to User
    public User convertToEntity(UserRegisterDto userRegisterDto) {
        return User.builder()
                .username(userRegisterDto.getUsername())
                .email(userRegisterDto.getEmail())
                .password(userRegisterDto.getPassword())
                .build();
    }

    // convert UserDto to User
    public User convertToEntity(UserDto userDto) {
        List<Task> tasks = taskRepository.findAllById(userDto.getTaskIds());
        return User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .tasks(tasks)

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
}
