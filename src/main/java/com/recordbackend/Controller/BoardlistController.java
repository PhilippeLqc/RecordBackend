package com.recordbackend.Controller;

import com.recordbackend.Dto.BoardlistDto;
import com.recordbackend.Service.BoardlistService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boardlist")
@AllArgsConstructor
public class BoardlistController {

    private final BoardlistService boardlistService;

    // create boardlist
    @PostMapping("/create")
    public BoardlistDto createProject(BoardlistDto boardlistDto){
        return boardlistService.createBoardlist(boardlistDto);
    }

    //get all boardlist from the database
    @GetMapping("/all")
    public List<BoardlistDto> getAllBoardlist(){
        return boardlistService.getAllBoardlist();
    }

    // get boardlist by id
    @GetMapping("/{id}")
    public BoardlistDto getBoardlistDtoById(@PathVariable Long id){
        return boardlistService.getBoardlistDtoById(id);
    }

    // Update boardlist by id
    @PutMapping("/{id}")
    public BoardlistDto updateBoardlist(@PathVariable Long id, @RequestBody BoardlistDto boardlistDto){
        return boardlistService.updateBoardlistById(id, boardlistDto);
    }

    // delete boardlist by id
    @DeleteMapping("/{id}")
    public void deleteBoardlist(@PathVariable Long id){
        boardlistService.deleteBoardlist(id);
    }

}
