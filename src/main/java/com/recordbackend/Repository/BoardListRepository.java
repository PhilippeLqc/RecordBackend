package com.recordbackend.Repository;

import com.recordbackend.Model.Boardlist;
import com.recordbackend.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardListRepository extends JpaRepository<Boardlist, Long> {
    @Query(value = "SELECT * FROM boardlist b WHERE b.name = ?1", nativeQuery = true)
    Boardlist findBoardlistByName(String name);

    @Query(value = "SELECT * FROM boardlist b WHERE b.project_id = ?1", nativeQuery = true)
    List<Boardlist> findBoardlistByProjectId(Long project_id);
}
