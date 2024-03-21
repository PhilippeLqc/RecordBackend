package com.recordbackend.Dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserRegisterDto{
    private String username;

    @NotNull
    private String password;

    @Email
    private String email;
}