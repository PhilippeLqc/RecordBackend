package com.recordbackend.Repository;

import com.recordbackend.Model.Boardlist;
import com.recordbackend.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardListRepository extends JpaRepository<Boardlist, Long> {
    @Query(value = "SELECT * FROM boardlist b WHERE b.name = ?1", nativeQuery = true)
    Boardlist findBoardlistByName(String name);

}
