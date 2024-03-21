package com.recordbackend.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class LogsDto {
    @Email
    String email;
    @NotNull
    String password;
}
