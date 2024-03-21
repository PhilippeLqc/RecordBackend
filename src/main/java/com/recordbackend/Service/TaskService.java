package com.recordbackend.Service;

import com.recordbackend.Dto.TaskDto;
import com.recordbackend.Model.Task;
import com.recordbackend.Model.User;
import com.recordbackend.Repository.BoardListRepository;
import com.recordbackend.Repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final BoardListRepository boardListRepository;

    public List<TaskDto> getAllTask(){
        return this.taskRepository.findAll().stream().map(this::convertToTaskDto).toList();
    }

    public TaskDto getTaskById(Long id){
        return this.convertToTaskDto(taskRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Task not found")));
    }

    public TaskDto createTask(TaskDto taskDto){
        return this.convertToTaskDto(this.taskRepository.save(this.convertToTaskEntity(taskDto)));
    }

    public TaskDto updateTask(Long id, TaskDto taskDto){
        Task taskToUpdate = this.convertToTaskEntity(taskDto);
        taskToUpdate.setId(id);
        return this.convertToTaskDto(taskToUpdate);
    }

    public void deleteTask(Long id){
        this.taskRepository.deleteById(id);
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
                .users(taskDto.getListUserId().stream().map(itemId -> userService.convertToEntity(userService.getUserById(itemId))).toList())
                .build();
    }

    //List<Task> task = userDto.getTaskIds().stream().map(taskService::findById).toList();

}
