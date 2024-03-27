package com.recordbackend.Controller;

import com.recordbackend.Dto.*;
import com.recordbackend.Service.EmailService;
import com.recordbackend.Service.JwtService;
import com.recordbackend.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<String> saveUser(@Valid @RequestBody UserRegisterDto userRegisterDto) {
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
    public void activateAccount(@Valid @RequestBody TokenRequest tokenRequest) {
        userService.activateAccount(tokenRequest.getToken());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(jwtService.refreshToken(tokenRequest));
    }

    @PostMapping("/password-forgot")
    public ResponseEntity<String> passwordForgot(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            emailService.sendPasswordForgotEmail(email);
            return ResponseEntity.ok("Email send with success");
        } catch (MessagingException e) {
            return new ResponseEntity<>("Mail send error.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/reset-password")
    public void resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        userService.resetPassword(resetPasswordRequest);
    }
}
