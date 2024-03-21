package com.recordbackend.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class LogsDto {
    @Email
    String email;
    @NotNull
    String password;
}
