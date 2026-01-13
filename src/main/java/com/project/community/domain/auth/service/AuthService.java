package com.project.community.domain.auth.service;

import static com.project.community.domain.auth.entity.RefreshToken.createRefreshToken;

import com.project.community.domain.auth.dto.LoginRequestDTO;
import com.project.community.domain.auth.dto.LoginResponseDTO;
import com.project.community.domain.auth.dto.RefreshTokenRequestDTO;
import com.project.community.domain.auth.dto.RefreshTokenResponseDTO;
import com.project.community.domain.auth.entity.RefreshToken;
import com.project.community.domain.auth.repository.RefreshTokenRepository;
import com.project.community.domain.member.entity.Member;
import com.project.community.domain.member.repository.MemberRepository;
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


    public LoginResponseDTO login(LoginRequestDTO request) {
        Member member = searchByIdAndEmail(request.getMemberId());
        checkPassword(request, member);

        String accessToken = jwtTokenProvider.generateAccessToken(member.getMemberId(), member.getMemberRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getMemberId());
        Date refreshTokenExpireDate = jwtTokenProvider.refreshTokenExpiration();

        refreshTokenRepository.save(createRefreshToken(request.getMemberId(), refreshToken, refreshTokenExpireDate));

        return new LoginResponseDTO(
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenExpiration(),
                member.getMemberId());
    }

    private void checkPassword(LoginRequestDTO request, Member member) {
        if (!passwordEncoder.matches(request.getPassword(), member.getMemberPwd())) {
            throw new BadCredentialsException("비밀번호가 맞지 않습니다.");
        }
    }

    private Member searchByIdAndEmail(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .or(() -> memberRepository.findByMemberEmail(memberId))
                .orElseThrow(() -> new UsernameNotFoundException("아이디 또는 이메일이 없습니다."));
    }

    public void logout(String memberId) {
        deleteTokenByMemberId(memberId);
    }

    private void deleteTokenByMemberId(String memberId) {
        refreshTokenRepository.deleteByMemberId(memberId);
    }

    public RefreshTokenResponseDTO createNewTokens(RefreshTokenRequestDTO request) {
        RefreshToken searchToken = findByRefreshToken(request);

        jwtTokenProvider.validateToken(searchToken.getRefreshToken());

        deleteTokenByMemberId(searchToken.getMemberId());

        Member member = searchByIdAndEmail(searchToken.getMemberId());

        String accessToken = jwtTokenProvider.generateAccessToken(member.getMemberId(), member.getMemberRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getMemberId());
        Date refreshTokenExpireDate = jwtTokenProvider.refreshTokenExpiration();

        refreshTokenRepository.save(createRefreshToken(member.getMemberId(), refreshToken, refreshTokenExpireDate));

        return new RefreshTokenResponseDTO(accessToken, refreshToken,jwtTokenProvider.getAccessTokenExpiration() );

    }

    private RefreshToken findByRefreshToken(RefreshTokenRequestDTO request) {
        return refreshTokenRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new NoSuchElementException("해당하는 Refresh 토큰은 존재하지 않습니다"));
    }


}
