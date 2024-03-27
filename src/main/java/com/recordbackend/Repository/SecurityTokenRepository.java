package com.recordbackend.Repository;

import com.recordbackend.Model.Reason;
import com.recordbackend.Model.SecurityToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityTokenRepository extends JpaRepository<SecurityToken, Long> {

    boolean existsByTokenAndIsDisabledIsFalse(String token);

    Optional<SecurityToken> findByToken(String token);

    @Query("SELECT s FROM SecurityToken s WHERE s.user.id = :userId AND s.reason = :reason AND s.isDisabled = false")
    SecurityToken findByUserIdAndReason(Long userId, Reason reason);
}
