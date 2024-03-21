package com.recordbackend.Dto;

import com.recordbackend.Model.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TaskDto {
    private String title;
    private String description;
    private LocalDateTime expirationDate;
    private Status status;
    private List<Long> listUserId;
    private Long boardlistId;
}
