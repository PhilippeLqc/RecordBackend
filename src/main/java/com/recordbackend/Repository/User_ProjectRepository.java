package com.recordbackend.Repository;

import com.recordbackend.Model.User_project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User_ProjectRepository extends JpaRepository<User_project, Long> {
}
