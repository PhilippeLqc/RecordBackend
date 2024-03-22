package com.recordbackend.Controller;

import com.recordbackend.Model.Project;
import com.recordbackend.Model.Status;
import com.recordbackend.Service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // create project
    @PostMapping("/create")
    public Project createProject(@RequestBody Project project){
        return projectService.createProject(project);
    }

    // get all projects
    @GetMapping("/all")
    public List<Project> getAllProjects(){
        return projectService.getAllProjects();
    }

    // get project by id
    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id){
        return projectService.findById(id);
    }

    // update project by id
    @PutMapping("/update/{id}")
    public Project updateProject(@PathVariable Long id, @RequestBody Project project){
        return projectService.updateProject(id, project);
    }

    // delete project by id
    @DeleteMapping("/delete/{id}")
    public void deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
    }

    // get all projects by status
    @GetMapping("/{status}")
    public List<Project> getAllProjectsByStatus(@PathVariable String status){
        return projectService.getAllProjectsByStatus(Status.valueOf(status));
    }

    // get all projects by status by user id
    @GetMapping("/{status}/user/{userId}")
    public List<Project> getAllProjectsByStatusAndUserId(@PathVariable String status, @PathVariable Long userId){
        return projectService.getAllProjectsByStatusAndUserId(Status.valueOf(status));
    }

    // get all projects by user id
    @GetMapping("/user/{userId}")
    public List<Project> getAllProjectsByUserId(@PathVariable Long userId){
        return projectService.getAllProjectsByUserId();
    }
}
