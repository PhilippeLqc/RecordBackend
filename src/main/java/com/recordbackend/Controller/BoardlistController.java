package com.recordbackend.Controller;

import com.recordbackend.Dto.BoardlistDto;
import com.recordbackend.Model.Project;
import com.recordbackend.Service.BoardlistService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/boardlist")
@AllArgsConstructor
public class BoardlistController {

    private final BoardlistService boardlistService;

    // create boardlist
    @PostMapping("/create")
    public BoardlistDto createProject(Project project){
        return boardlistService.createBoardlist(project);
    }
}
