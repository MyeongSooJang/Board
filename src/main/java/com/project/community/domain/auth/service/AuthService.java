package com.project.community.domain.auth.service;

import com.project.community.domain.auth.dto.LoginRequestDTO;
import com.project.community.domain.auth.dto.LoginResponseDTO;
import com.project.community.domain.auth.entity.RefreshToken;
import com.project.community.domain.auth.repository.RefreshTokenRepository;
import com.project.community.domain.member.entity.Member;
import com.project.community.domain.member.repository.MemberRepository;
import com.project.community.security.JwtTokenProvider;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public LoginResponseDTO login(LoginRequestDTO request) {
        Member member = memberRepository.findByMemberId(request.getMemberId())
                .or(() -> memberRepository.findByMemberEmail(request.getMemberId()))
                .orElseThrow(() -> new UsernameNotFoundException("아이디 또는 이메일이 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getMemberPwd())) {
            throw new BadCredentialsException("비밀번호가 맞지 않습니다.");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(member.getMemberId(), member.getMemberRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(member.getMemberId());
        Date refreshTokenExpireDate = jwtTokenProvider.refreshTokenExpiration();

        refreshTokenRepository.save()


    }

    public RefreshToken createRefreshToken(LoginRequestDTO request) {

    }


}
