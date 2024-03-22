package com.recordbackend.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class SecurityToken {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String token;
    @Enumerated(EnumType.STRING)
    private Reason reason;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private boolean isDisabled = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
