package com.recordbackend.Repository;

import com.recordbackend.Model.SecurityToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityTokenRepository extends JpaRepository<SecurityToken, Long> {

    boolean existsByTokenAndIsDisabledIsFalse(String token);

    Optional<SecurityToken> findByToken(String token);
}
