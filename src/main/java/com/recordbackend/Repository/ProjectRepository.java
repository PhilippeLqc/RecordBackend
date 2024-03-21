package com.recordbackend.Repository;

import com.recordbackend.Model.Project;
import com.recordbackend.Model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByStatus(Status status);
}
