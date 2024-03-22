package com.recordbackend.Repository;

import com.recordbackend.Model.Boardlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardListRepository extends JpaRepository<Boardlist, Long> {
}
