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
    String username;
    String email;
    Role role;
    List<Long> taskIds;
    List<Long> projectIds;
}