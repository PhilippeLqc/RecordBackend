package com.recordbackend.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenResponseDto {

    @NotNull
    private String token;

    @NotNull
    private String refreshToken;
}
