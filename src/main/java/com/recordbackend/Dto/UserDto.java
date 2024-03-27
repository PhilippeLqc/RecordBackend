package com.recordbackend.Dto;

import com.recordbackend.Model.Role;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDto{
    private Long id;
    private String username;
    private String email;
    private Role role;
    private List<Long> taskIds;
    private List<Long> projectIds;
}