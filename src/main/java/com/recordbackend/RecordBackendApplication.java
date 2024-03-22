package com.recordbackend;

import com.recordbackend.Model.Role;
import com.recordbackend.Model.User;
import com.recordbackend.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@AllArgsConstructor
public class RecordBackendApplication implements CommandLineRunner {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(RecordBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User adminAccount = userRepository.findByEmail("admin@me.fr");
        if (adminAccount == null) {
            User user = new User();

            user.setEmail("admin@me.fr");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setRole(Role.ADMIN);
            user.setEnable(true);
            userRepository.save(user);
        }
    }
}
