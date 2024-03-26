package com.recordbackend.Controller;

import com.recordbackend.Dto.TaskDto;
import com.recordbackend.Service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Long id) {
        TaskDto task = taskService.getTaskDtoById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<TaskDto> tasks = taskService.getAllTask();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/user/{id}")
    public List<TaskDto> getTaskDtoByUserId(@PathVariable("id") Long id) {
        return this.taskService.getTaskDtoByUserId(id);
    }

    @GetMapping("/keyword/{keyword}")
    public List<TaskDto> getTaskByTitleOrDescription(@PathVariable("keyword") String keyword){
        return this.taskService.retrieveTaskByTitleOrDescription(keyword);
    }

    @PostMapping("/create")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto task){
        TaskDto newTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @RequestBody TaskDto task) {
        TaskDto updatedTask = taskService.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
