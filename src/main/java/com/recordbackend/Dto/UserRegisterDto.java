package com.recordbackend.Dto;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserRegisterDto{
    private String username;
    private String password;
    private String email;
}