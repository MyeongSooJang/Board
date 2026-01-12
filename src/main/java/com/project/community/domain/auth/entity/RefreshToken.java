package com.project.community.domain.auth.entity;

import com.project.community.domain.auth.dto.LoginRequestDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenNo;
    private String memberId;
    private String refreshToken;
    private Date expireDate;

    public RefreshToken(String memberId, String refreshToken, Date expireDate) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
        this.expireDate =  expireDate;
    }

    public static RefreshToken createRefreshToken(String memberId, String refreshToken, Date expireDate) {
        return new RefreshToken(memberId, refreshToken, expireDate);
    }
}
