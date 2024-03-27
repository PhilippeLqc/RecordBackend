package com.recordbackend.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String message;

    @ManyToOne
    private Project project;

    @ManyToOne
    private User user;

}
