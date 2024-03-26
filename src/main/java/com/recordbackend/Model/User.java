package com.recordbackend.Model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean isEnable = false;

    @Enumerated(EnumType.STRING)
    private Role role;


    @ManyToMany(mappedBy = "users")
    private List<Task> tasks;

    @OneToMany(mappedBy = "user")
    private List<SecurityToken> securityTokens;

    @OneToMany(mappedBy = "user")
    private List<User_project> user_projects;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }

    public List<Long> getTaskIds() {
        return tasks.stream().map(Task::getId).toList();
    }

    public List<Long> getProjectIds() {
        return user_projects.stream().map(User_project::getProject).map(Project::getId).toList();
    }
}
