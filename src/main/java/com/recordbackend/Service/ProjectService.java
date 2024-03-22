package com.recordbackend.Service;

import com.recordbackend.Model.Project;
import com.recordbackend.Model.Status;
import com.recordbackend.Model.User_project;
import com.recordbackend.Repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;

    // find by ID a list of projects
    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }

    // save a project
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    // get all projects
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // delete a project
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    // update a project
    public Project updateProject(Long id, Project project) {
        Project projectToUpdate = findById(id);
        projectToUpdate.setTitle(project.getTitle());
        projectToUpdate.setDescription(project.getDescription());
        projectToUpdate.setStatus(project.getStatus());
        return projectRepository.save(projectToUpdate);
    }

    // get all projects by status
    public List<Project> getAllProjectsByStatus(Status status) {
        return projectRepository.findAllByStatus(status);
    }

    // get all projects by status by userDto
    public List<Project> getAllProjectsByStatusAndUserId(Status status, Long userId) {
        return userService.getUserById(userId)
                .getUser_projects()
                .stream()
                .map(User_project::getProject)
                .filter(project -> project.getStatus().equals(status))
                .toList();
    }

    // get all projects by userDto
    public List<Project> getAllProjectsByUserId(Long userId) {
        return userService.getUserById(userId)
                .getUser_projects()
                .stream()
                .map(User_project::getProject)
                .toList();
    }
}
