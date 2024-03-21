package com.recordbackend.Service;

import com.recordbackend.Model.Reason;
import com.recordbackend.Model.SecurityToken;
import com.recordbackend.Model.User;
import com.recordbackend.Repository.SecurityTokenRepository;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
public class SecurityTokenService {
    private final SecurityTokenRepository securityTokenRepository;
    private final JwtService jwtService;

    // Method to save the token from the database
    public void saveToken(SecurityToken token) {
        securityTokenRepository.save(token);
    }

    // Method to create & save securityToken from jwt
    public void createTokenFromJwt(String jwt, Reason reason, User user) {
        Claims claims = jwtService.extractAllClaims(jwt);

        SecurityToken token = SecurityToken.builder()
                .token(jwt)
                .reason(reason)
                .createdAt(claims.getIssuedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .expiredAt(claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .user(user)
                .build();

        saveToken(token);
    }

    // Method to save the jwt and refresh token
    public void saveJwtAndRefreshToken(String jwt, String refreshToken, User user) {
        createTokenFromJwt(jwt, Reason.JWT, user);
        createTokenFromJwt(refreshToken, Reason.REFRESH_TOKEN, user);
    }

    // Method to check if the token exists and is activated
    public boolean tokenExistsAndIsActivated(String token) {
        return securityTokenRepository.existsByTokenAndIsDisabledIsFalse(token);
    }

    // Method to delete the token from the database
    public void deleteTokenByUser(User user) {
        List<SecurityToken> tokensToDelete = user.getSecurityTokens()
                .stream()
                .filter(token -> token.getReason() == Reason.JWT || token.getReason() == Reason.REFRESH_TOKEN)
                .toList();
        securityTokenRepository.deleteAll(tokensToDelete);
    }
}
