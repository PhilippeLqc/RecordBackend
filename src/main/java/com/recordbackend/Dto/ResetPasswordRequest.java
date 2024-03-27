package com.recordbackend.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResetPasswordRequest {

    @NotNull
    private String token;

    @NotNull
    @Size(min = 4)
    private String password;

    @NotNull
    @Size(min = 4)
    private String confirmPassword;
}
