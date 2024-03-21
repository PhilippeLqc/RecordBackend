package com.recordbackend.exeption;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private Object errors;
}