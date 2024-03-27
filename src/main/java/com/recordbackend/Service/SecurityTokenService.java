package com.recordbackend.Service;

import com.recordbackend.Model.Reason;
import com.recordbackend.Model.SecurityToken;
import com.recordbackend.Model.User;
import com.recordbackend.Repository.SecurityTokenRepository;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityTokenService {
    private final SecurityTokenRepository securityTokenRepository;
    private final JwtService jwtService;
    @Value("${FRONTEND_URL}")
    private String frontUrl;
    private static final String EMAIL_VERIFICATION = "/email-verification?token=";
    private static final String RESET_PASSWORD = "reset-password?token=";


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

    public String generateVerificationLink(User user) {
        String token = generateUniqueToken();

        createToken(token, user, Reason.VERIFY_EMAIL);
        return frontUrl + EMAIL_VERIFICATION + token;
    }

    private String generateUniqueToken() {
        byte[] tokenBytes = new byte[32];
        new SecureRandom().nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    private void createToken(String token, User user, Reason reason) {
        SecurityToken securityToken = SecurityToken.builder()
                .token(token)
                .reason(reason)
                .createdAt(java.time.LocalDateTime.now())
                .expiredAt(java.time.LocalDateTime.now())
                .user(user)
                .build();

        saveToken(securityToken);
    }

    public void deleteToken(SecurityToken token) {
        securityTokenRepository.delete(token);
    }

    public SecurityToken getToken(String token) {
        return securityTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Token not found"));
    }

    public String generateResetPasswordLink(User user) {
        SecurityToken currentToken = securityTokenRepository.findByUserIdAndReason(user.getId(), Reason.RESET_PASSWORD);
        if (currentToken != null) {
            deleteToken(currentToken);
        }

        String token = generateUniqueToken();

        createToken(token, user, Reason.RESET_PASSWORD);
        return frontUrl + RESET_PASSWORD + token;
    }
}
