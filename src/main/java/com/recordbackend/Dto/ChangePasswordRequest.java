package com.recordbackend.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotNull
    private String currentPassword;

    @NotNull
    @Size(min = 4)
    private String password;

    @NotNull
    @Size(min = 4)
    private String confirmPassword;
}
