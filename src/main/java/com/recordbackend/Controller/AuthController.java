package com.recordbackend.Controller;

import com.recordbackend.Dto.AuthResponseDto;
import com.recordbackend.Dto.LogsDto;
import com.recordbackend.Dto.UserDto;
import com.recordbackend.Dto.UserRegisterDto;
import com.recordbackend.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserDto saveUser(@Valid @RequestBody UserRegisterDto userRegisterDto){
        return userService.createUser(userRegisterDto);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LogsDto logsDto) {
        return userService.login(logsDto);
    }

    @PostMapping("/logout")
    public void logout() {
        userService.logout();
    }
}
