package com.project.community.auth.service;

import static com.project.community.auth.entity.RefreshToken.createRefreshToken;

import com.project.community.auth.dto.LoginRequest;
import com.project.community.auth.dto.LoginResponse;
import com.project.community.auth.dto.RefreshTokenRequest;
import com.project.community.auth.dto.RefreshTokenResponse;
import com.project.community.auth.entity.RefreshToken;
import com.project.community.auth.repository.RefreshTokenRepository;
import com.project.community.member.entity.Member;
import com.project.community.member.repository.MemberRepository;
import com.project.community.security.JwtTokenProvider;
import java.util.Date;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public LoginResponse login(LoginRequest request) {
        Member member = searchByIdAndEmail(request.getUsername());
        checkPassword(request, member);

        String accessToken = jwtTokenProvider.generateAccessToken(member.getUsername(), member.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getUsername());
        Date refreshTokenExpireDate = jwtTokenProvider.refreshTokenExpiration();

        refreshTokenRepository.save(createRefreshToken(request.getUsername(), refreshToken, refreshTokenExpireDate));

        return new LoginResponse(
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenExpiration(),
                member.getUsername(),
                member.getName(),
                member.getRole()
        );
    }

    private void checkPassword(LoginRequest request, Member member) {
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 맞지 않습니다.");
        }
    }

    private Member searchByIdAndEmail(String usernameOrEmail) {
        return memberRepository.findByUsernameAndDeleteTimeIsNull(usernameOrEmail)
                .or(() -> memberRepository.findByEmailAndDeleteTimeIsNull(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("아이디 또는 이메일이 없습니다."));
    }

    public void logout(String username) {
        deleteTokenByUsername(username);
    }

    private void deleteTokenByUsername(String username) {
        refreshTokenRepository.deleteByUsername(username);
    }

    public RefreshTokenResponse createNewTokens(RefreshTokenRequest request) {
        RefreshToken searchToken = findByRefreshToken(request);

        jwtTokenProvider.validateToken(searchToken.getRefreshToken());

        deleteTokenByUsername(searchToken.getUsername());

        Member member = searchByIdAndEmail(searchToken.getUsername());

        String accessToken = jwtTokenProvider.generateAccessToken(member.getUsername(), member.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getUsername());
        Date refreshTokenExpireDate = jwtTokenProvider.refreshTokenExpiration();

        refreshTokenRepository.save(createRefreshToken(member.getUsername(), refreshToken, refreshTokenExpireDate));

        return new RefreshTokenResponse(accessToken, refreshToken, jwtTokenProvider.getAccessTokenExpiration());

    }

    private RefreshToken findByRefreshToken(RefreshTokenRequest request) {
        return refreshTokenRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new NoSuchElementException("해당하는 Refresh 토큰은 존재하지 않습니다"));
    }


}
