package com.recordbackend.Dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BoardlistDto {

    private Long id;
    private String name;
    private Long projectId;
}
