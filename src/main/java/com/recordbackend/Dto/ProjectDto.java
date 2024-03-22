package com.recordbackend.Dto;

import com.recordbackend.Model.Status;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProjectDto {
    Long id;
    String title;
    Status status;
}