package com.recordbackend.Service;

import com.recordbackend.Dto.TaskDto;
import com.recordbackend.Model.Task;
import com.recordbackend.Model.User;
import com.recordbackend.Repository.BoardListRepository;
import com.recordbackend.Repository.TaskRepository;
import com.recordbackend.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        Task task = Task.builder()
                .id(taskDto.getTaskId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .expirationDate(taskDto.getExpirationDate())
                .status(taskDto.getStatus())
                .boardlist(this.boardListRepository.findById(taskDto.getBoardlistId()).orElseThrow(() -> new EntityNotFoundException("BoardList not found")))
                .users(new ArrayList<>())
                .hierarchy(taskDto.getHierarchy())
                .build();

        task.getUsers().addAll(taskDto.getListUserId().stream().map(userId -> userService.getUserById(userId)).toList());
        return task;
    }

    // convert Task to TaskDto
    public TaskDto convertToTaskDto(Task task){
        return TaskDto.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .expirationDate(task.getExpirationDate())
                .status(task.getStatus())
                .hierarchy(task.getHierarchy())
                .listUserId(task.getUsers().stream().map(User::getId).toList())
                .boardlistId(this.boardListRepository.findById(task.getBoardlist().getId()).get().getId())
                .build();
    }

    //------------------------------------------------------------------------------------------------------------
    //
    // create a new user
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
        return this.convertToTaskDto(this.taskRepository.save(taskToUpdate));
    }

    public List<TaskDto> retrieveTaskByTitleOrDescription(String keyword){
        return this.taskRepository.findTasksByTitleOrDescription(keyword).stream().map(this::convertToTaskDto).toList();
    }

    // delete Task by id
    public void deleteTask(Long id){
        this.taskRepository.deleteById(id);
    }

//    public TaskDto assignUserToTask(Long userId, Long taskId) {
//        User userRetrieved = this.userService.getUserById(userId);
//        Task task = this.taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));
//        if(task.getUsers().stream().noneMatch(user -> user.getId().equals(userId))){
//            task.getUsers().add(userRetrieved);
//            return this.convertToTaskDto(this.taskRepository.save(task));
//        }
//        return this.convertToTaskDto(task);
//    }


//    public ResponseEntity<?> assignUserToTask(Long userId, Long taskId) {
//        User userRetrieved = this.userService.getUserById(userId);
//        Task task = this.taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));
//        if(task.getUsers().stream().noneMatch(user -> user.getId().equals(userId))){
//            task.getUsers().add(userRetrieved);
//            TaskDto taskDto = this.convertToTaskDto(this.taskRepository.save(task));
//            return new ResponseEntity<>(taskDto, HttpStatus.OK);
//        }
//        return new ResponseEntity<>("User is already assigned to the task", HttpStatus.CONFLICT);
//    }

    public ResponseEntity<TaskDto> assignUserToTask(Long userId, Long taskId) {
        User userRetrieved = this.userService.getUserById(userId);
        Task task = this.taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        if(task.getUsers().stream().anyMatch(user -> user.getId().equals(userId))){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        task.getUsers().add(userRetrieved);
        TaskDto taskDto = this.convertToTaskDto(this.taskRepository.save(task));
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }

    public Boolean removeUserFromTask(Long userId, Long taskId) {
        User userRetrieved = this.userService.getUserById(userId);
        Task task = this.taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        if(task.getUsers().stream().anyMatch(user -> user.getId().equals(userId))) {

            task.getUsers().remove(userRetrieved); // Remove the User from the Task's user list

            this.taskRepository.save(task); // Save the Task
            return true;
        }
        return false;
    }
}
