package com.recordbackend.Service;

import com.recordbackend.Dto.TaskDto;
import com.recordbackend.Model.Boardlist;
import com.recordbackend.Model.Task;
import com.recordbackend.Model.User;
import com.recordbackend.Repository.BoardListRepository;
import com.recordbackend.Repository.TaskRepository;
import com.recordbackend.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final BoardListRepository boardListRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, BoardListRepository boardListRepository){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.boardListRepository = boardListRepository;
    }

    public List<Task> getAllTask(){
        return this.taskRepository.findAll();
    }

    public TaskDto getTaskById(Long id){
        return this.convertToTaskDto(taskRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Task not found")));
    }

    public TaskDto createTask(TaskDto taskDto){
        return this.convertToTaskDto(this.taskRepository.save(this.convertToTaskEntity(taskDto)));
    }




    // Convert to TaskDto
    public TaskDto convertToTaskDto(Task task){
        return TaskDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .expirationDate(task.getExpirationDate())
                .status(task.getStatus())
                .listUserId(task.getUsers().stream().map(User::getId).toList())
                .boardlistId(this.boardListRepository.findById(task.getBoardlist().getId()).get().getId())
                .build();
    }

    // Convert to Task Entity
    public Task convertToTaskEntity(TaskDto taskDto){
        Task task = this.taskRepository.findTaskByTitle(taskDto.getTitle());

        return Task.builder()
                .id(task.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .expirationDate(taskDto.getExpirationDate())
                .status(taskDto.getStatus())
                .boardlist(this.boardListRepository.findById(taskDto.getBoardlistId()).orElseThrow(() -> new EntityNotFoundException("BoardList not found")))
                .users(taskDto.getListUserId().stream().map(userService::findById).toList())
                .build();
    }

    //List<Task> task = userDto.getTaskIds().stream().map(taskService::findById).toList();

}
