package com.recordbackend.Controller;

import com.recordbackend.Dto.AuthResponseDto;
import com.recordbackend.Dto.LogsDto;
import com.recordbackend.Dto.TokenRequest;
import com.recordbackend.Dto.UserRegisterDto;
import com.recordbackend.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> saveUser(@Valid @RequestBody UserRegisterDto userRegisterDto){
        try {
            userService.createUser(userRegisterDto);
            return new ResponseEntity<>("Utilisateur enregistré avec succès.", HttpStatus.CREATED);
        } catch (MessagingException e) {
            return new ResponseEntity<>("Erreur lors de l'envoi de l'e-mail.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LogsDto logsDto) {
        return userService.login(logsDto);
    }

    @PostMapping("/logout")
    public void logout() {
        userService.logout();
    }

    @PutMapping("/activate")
    public void activateAccount(@RequestBody TokenRequest tokenRequest) {
        userService.activateAccount(tokenRequest.getToken());
    }
}
