package com.recordbackend.Dto;


import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserRegisterDto{
    String username;
    String password;
    String email;
}