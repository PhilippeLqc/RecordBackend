package com.recordbackend.Service;

import com.recordbackend.Dto.BoardlistDto;
import com.recordbackend.Model.Boardlist;
import com.recordbackend.Repository.BoardListRepository;
import com.recordbackend.Repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardlistService {

    private final BoardListRepository boardListRepository;
    private final ProjectRepository projectRepository;


    // Convert Boardlist to BoardlistDto
    public BoardlistDto convertToBoardlistDto(Boardlist boardlist){
        return BoardlistDto.builder()
                .name(boardlist.getName())
                .projectId(boardlist.getProject().getId())
                .build();
    }

    // Convert BoardlistDto to Boardlist Entity
    public Boardlist convertToBoardlistEntity(BoardlistDto boardlistDto){
        Boardlist boardlist = this.boardListRepository.findBoardlistByName(boardlistDto.getName());

        return Boardlist.builder()
                .id(boardlist.getId())
                .name(boardlistDto.getName())
                .project(this.projectRepository.findById(boardlistDto.getProjectId()).get())
                .build();
    }

    public List<BoardlistDto> getAllBoardlist() {

    }







}
