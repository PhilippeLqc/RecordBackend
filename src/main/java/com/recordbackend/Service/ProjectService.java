package com.recordbackend.Service;

import com.recordbackend.Dto.ProjectDto;
import com.recordbackend.Model.*;
import com.recordbackend.Repository.ProjectRepository;
import com.recordbackend.Repository.UserRepository;
import com.recordbackend.Repository.User_ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service

@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final User_ProjectRepository user_projectRepository;
    private final UserRepository userRepository;
    private final User_ProjectRepository userProjectRepository;

    @Setter
    @Autowired
    private UserService userService;

//converter to convert ProjectDto to Project and vice versa
private Project convertToEntity(ProjectDto projectDto) {
    return Project.builder()
            .id(projectDto.getId())
            .title(projectDto.getTitle())
            .status(projectDto.getStatus())
            .build();
}

private ProjectDto convertToDto(Project project) {
    return ProjectDto.builder()
            .id(project.getId())
            .title(project.getTitle())
            .status(project.getStatus())
            .build();
}


    // find by ID a list of projects
    public Project findById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }

    // save a project
    public Project createProject(Project project) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Project projectSave = Project.builder()
                .title(project.getTitle())
                .description(project.getDescription())
                .status(project.getStatus())
                .user_projects(new ArrayList<>())
                .build();
        // save user into project user_projects
        User_project user_project = User_project.builder()
                .project(projectSave)
                .user(user)
                .role(Role.ADMIN)
                .build();
        // first add user_project in the project
        projectSave.getUser_projects().add(user_project);
        // second save the project with user's creator
        projectRepository.save(projectSave);
        // third, make the jointure between user's creator & project
        user_projectRepository.save(user_project);
        // save project
        return project;
    }


    // delete a project
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    // update a project
    public Project updateProject(Long id, Project project) {
        // find project by id and update project title, description and status and save it if needed.
        Project projectToUpdate = findById(id);
        projectToUpdate.setTitle(project.getTitle());
        projectToUpdate.setDescription(project.getDescription());
        projectToUpdate.setStatus(project.getStatus());
        return projectRepository.save(projectToUpdate);
    }

    // get all projects by status
    public List<Project> getAllProjectsByStatus(Status status) {
        List<ProjectDto> projectDtos = projectRepository.findAllByStatus(status)
                .stream()
                .map(this::convertToDto)
                .toList();
        return projectDtos.stream().map(this::convertToEntity).toList();
    }

    // get all projects only if user is admin
    public List<Project> getAllProjects() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ProjectDto> projectDtos = userService.getUserById(user.getId())
                .getUser_projects()
                .stream()
                .map(User_project::getProject)
                .map(this::convertToDto)
                .toList();
        return projectDtos.stream().map(this::convertToEntity).toList();
    }
    // get all projects by status by user
    public List<Project> getAllProjectsByStatusAndUserId(Status status) {
        // initialize user defining by jwt
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ProjectDto> projectDtos = userService.getUserById(user.getId())
                .getUser_projects()
                .stream()
                .map(User_project::getProject)
                .filter(project -> project.getStatus().equals(status))
                .map(this::convertToDto)
                .toList();
        return projectDtos.stream().map(this::convertToEntity).toList();
    }
    // get all projects by user
    public List<Project> getAllProjectsByUserId() {
        // initialize user defining by jwt
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ProjectDto> projectDtos = userService.getUserById(user.getId())
                .getUser_projects()
                .stream()
                .map(User_project::getProject)
                .map(this::convertToDto)
                .toList();
        return projectDtos.stream().map(this::convertToEntity).toList();
    }
}

