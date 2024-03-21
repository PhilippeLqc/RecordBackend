package com.recordbackend.Repository;

import com.recordbackend.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT * FROM task t WHERE t.title = ?1", nativeQuery = true)
    Task findTaskByTitle(String title);
}
