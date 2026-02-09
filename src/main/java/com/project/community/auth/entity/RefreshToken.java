package com.project.community.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    private String username;
    private String refreshToken;
    private Date expireDate;

    public RefreshToken(String username, String refreshToken, Date expireDate) {
        this.username = username;
        this.refreshToken = refreshToken;
        this.expireDate = expireDate;
    }

    public static RefreshToken createRefreshToken(String username, String refreshToken, Date expireDate) {
        return new RefreshToken(username, refreshToken, expireDate);
    }
}
