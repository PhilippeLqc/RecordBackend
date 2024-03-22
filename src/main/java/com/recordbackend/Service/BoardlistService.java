package com.recordbackend.Service;

import com.recordbackend.Dto.BoardlistDto;
import com.recordbackend.Dto.UserDto;
import com.recordbackend.Model.Boardlist;
import com.recordbackend.Model.User;
import com.recordbackend.Repository.BoardListRepository;
import com.recordbackend.Repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BoardlistService {

    private final BoardListRepository boardListRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;


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

    //------------------------------------------------------------------------------------------------------------
    //
    // create a new user
    public BoardlistDto createBoardlist(BoardlistDto boardlistDto){
        return this.convertToBoardlistDto(this.boardListRepository.save(this.convertToBoardlistEntity(boardlistDto)));
    }

    // get all Boardlist
    public List<BoardlistDto> getAllBoardlist() {
        return this.boardListRepository.findAll().stream().map(this::convertToBoardlistDto).toList();
    }

    // get boardlistDto by id
    public BoardlistDto getBoardlistDtoById(Long id){
        return this.convertToBoardlistDto(this.getBoardlistById(id));
    }

    // get boardlist entity by id
    public Boardlist getBoardlistById(Long id){
        return this.boardListRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Boardlist not found"));
    }

    // update boardlist by id
    public BoardlistDto updateBoardlistById(Long id, BoardlistDto boardlistDto) {
        Boardlist boardlist = boardListRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Boardlist not found"));
        boardlist.setName(boardlistDto.getName());
        boardlist.setProject(this.projectService.findById(boardlistDto.getProjectId()));
        return convertToBoardlistDto(boardListRepository.save(boardlist));
    }

    // delete boardlist by id
    public void deleteBoardlist(Long id){
        this.boardListRepository.deleteById(id);
    }
}
