package com.recordbackend.Service;


import com.recordbackend.Dto.RefreshTokenResponseDto;
import com.recordbackend.Dto.TokenRequest;
import com.recordbackend.Model.User;
import com.recordbackend.Repository.SecurityTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    // 30 minutes
    private final long JWT_LIFETIME = 30 * 60 * 1000;
    // 8 hours
    private final long REFRESH_JWT_LIFETIME = 8 * 60 * 60 * 1000;
    private final SecurityTokenRepository securityTokenRepository;
    @Setter
    private UserService userService;

    @Value("${JWT_SECRET}")
    private String JWT_SECRET;

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_LIFETIME))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Map<String, Object> extractClaims, User user) {
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_JWT_LIFETIME))
                .signWith(getSiginKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> ClaimResolver) {
        final Claims claims = extractAllClaims(token);
        return ClaimResolver.apply(claims);
    }

    private Key getSiginKey() {
        byte[] key = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(key);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSiginKey()).build().parseClaimsJws(token).getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    public RefreshTokenResponseDto refreshToken(TokenRequest tokenRequest) {
        try {
            String userEmail = extractUserName(tokenRequest.getToken());
            User user = userService.convertToEntity(userService.getUserByEmail(userEmail));

            isTokenValid(tokenRequest.getToken(), user);

            String jwt = generateToken(user);

            return RefreshTokenResponseDto.builder()
                    .token(jwt)
                    .refreshToken(tokenRequest.getToken())
                    .build();
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, "Refresh token expired");
        }
    }
}
