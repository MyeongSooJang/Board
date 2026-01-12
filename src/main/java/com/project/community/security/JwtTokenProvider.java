package com.project.community.security;

import com.project.community.exception.TokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Long accessTokenExpiration;
    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String memberId, String role) {
        return Jwts.builder()
                .subject(memberId)
                .claim("role", role)
                .issuedAt(now())
                .expiration(accessTokenExpiration())
                .signWith(key)
                .compact();
    }

    private Date now() {
        return new Date(System.currentTimeMillis());
    }

    private Date accessTokenExpiration() {
        return new Date(System.currentTimeMillis() + accessTokenExpiration);
    }

    public String generateRefreshToken(String memberId) {
        return Jwts.builder()
                .subject(memberId)
                .issuedAt(now())
                .expiration(refreshTokenExpiration())
                .signWith(key)
                .compact();
    }

    public Date refreshTokenExpiration() {
        return new Date(System.currentTimeMillis() + refreshTokenExpiration);
    }


    public String getMemberId(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
        } catch (ExpiredJwtException e) {
            throw new TokenException("토큰이 만료되었습니다", "EXPIRED_TOKEN");
        } catch (SignatureException e) {
            throw new TokenException("유효하지 않은 토큰입니다", "INVALID_TOKEN");
        } catch (MalformedJwtException e) {
            throw new TokenException("잘못된 형식의 토큰입니다", "MALFORMED_JWT_TOKEN");
        } catch (IllegalArgumentException e) {
            throw new TokenException("토큰이 없습니다", "EMPTY_TOKEN");
        }
    }
}
