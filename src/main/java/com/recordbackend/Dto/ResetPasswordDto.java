package com.recordbackend.Dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ResetPasswordDto {
    private String token;
    private String password;
    private String confirmPassword;
}
