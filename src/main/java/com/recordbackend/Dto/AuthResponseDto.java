package com.recordbackend.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponseDto {
    private String token;

    private String refreshToken;
}
