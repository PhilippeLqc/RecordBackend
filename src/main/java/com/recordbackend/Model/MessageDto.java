package com.recordbackend.Model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MessageDto{
    Long id;
    String message;
    Long projectId;
    Long userId;
}