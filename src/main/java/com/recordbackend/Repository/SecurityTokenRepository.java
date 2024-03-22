package com.recordbackend.Repository;

import com.recordbackend.Model.SecurityToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityTokenRepository extends JpaRepository<SecurityToken, Long> {

    boolean existsByTokenAndIsDisabledIsFalse(String token);
}
