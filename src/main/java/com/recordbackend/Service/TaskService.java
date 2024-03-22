package com.recordbackend.Service;

import com.recordbackend.Dto.TaskDto;
import com.recordbackend.Model.Task;
import com.recordbackend.Model.User;
import com.recordbackend.Repository.BoardListRepository;
import com.recordbackend.Repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.oauth2.client.OAuth2ClientSecurityMarker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final BoardListRepository boardListRepository;

    @Setter
    @Autowired
    private UserService userService;

    // convert TaskDto to Task
    public Task convertToTaskEntity(TaskDto taskDto){
        Task task = this.taskRepository.findTaskByTitle(taskDto.getTitle());
        if(task == null){
            task = new Task();
        }

        return Task.builder()
                .id(task.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .expirationDate(taskDto.getExpirationDate())
                .status(taskDto.getStatus())
                .boardlist(this.boardListRepository.findById(taskDto.getBoardlistId()).orElseThrow(() -> new EntityNotFoundException("BoardList not found")))
                .users(taskDto.getListUserId().stream().map(itemId -> userService.getUserById(itemId)).toList())
                .build();
    }

    // convert Task to TaskDto
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

    //------------------------------------------------------------------------------------------------------------
    //
    // create a new task
    public TaskDto createTask(TaskDto taskDto){
        return this.convertToTaskDto(this.taskRepository.save(this.convertToTaskEntity(taskDto)));
    }

    // get all TaskDto
    public List<TaskDto> getAllTask(){
        return this.taskRepository.findAll().stream().map(this::convertToTaskDto).toList();
    }


    // get Task by id
    public Task getTaskById(Long id){
        return this.taskRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Task not found"));
    }

    // get TaskDto by id
    public TaskDto getTaskDtoById(Long id){
        return this.convertToTaskDto(taskRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Task not found")));
    }

    // get TaskDto by user id
    public List<TaskDto> getTaskDtoByUserId(Long id){
        return this.userService.getUserById(id).getTasks().stream().map(this::convertToTaskDto).toList();
    }

    // update Task by id
    public TaskDto updateTask(Long id, TaskDto taskDto){
        Task taskToUpdate = this.convertToTaskEntity(taskDto);
        taskToUpdate.setId(id);
        return this.convertToTaskDto(taskToUpdate);
    }

    // delete Task by id
    public void deleteTask(Long id){
        this.taskRepository.deleteById(id);
    }
}
