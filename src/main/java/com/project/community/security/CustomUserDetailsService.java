package com.project.community.security;

import com.project.community.domain.member.entity.Member;
import com.project.community.domain.member.repository.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> member = checkIdAndEmail(username);
        Member foundMember = member.orElseThrow(() ->
                new UsernameNotFoundException("사용자를 찾을수가 없습니다" + username));
        return User.builder()
                .username(foundMember.getMemberId())
                .password(foundMember.getMemberPwd())
                .roles(foundMember.getMemberRole())
                .build();
    }

    private Optional<Member> checkIdAndEmail(String username) {
        Optional<Member> member = memberRepository.findByMemberId(username);
        if (member.isEmpty()) {
            member = memberRepository.findByMemberEmail(username);
        }
        return member;
    }

}
