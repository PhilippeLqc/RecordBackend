package com.recordbackend.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TokenRequest {

    @NotNull
    private String token;
}
