package com.recordbackend.Service;

import com.recordbackend.Model.Project;
import com.recordbackend.Repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    // find by ID a list of projects
    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }
}
