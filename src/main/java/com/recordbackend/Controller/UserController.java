package com.recordbackend.Controller;

import com.recordbackend.Dto.UserDto;
import com.recordbackend.Dto.UserRegisterDto;
import com.recordbackend.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDto saveUser(UserRegisterDto userRegisterDto){
        return userService.createUser(userRegisterDto);
    }

}
