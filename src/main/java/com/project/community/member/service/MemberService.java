package com.project.community.member.service;

import static com.project.community.member.entity.Member.createMember;

import com.project.community.exception.NotFoundException;
import com.project.community.member.dto.MemberCreateRequest;
import com.project.community.member.dto.MemberResponse;
import com.project.community.member.dto.MemberUpdateRequest;
import com.project.community.member.entity.Member;
import com.project.community.member.repository.MemberRepository;
import com.project.community.exception.DuplicateException;
import com.project.community.exception.dto.ErrorCode;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse enrollMember(MemberCreateRequest request) {
        validateDuplicateUsername(request);
        validateDuplicatePhone(request);
        validateDuplicateEmail(request);
        String pwd = encodePassword(request.getPassword());
        return MemberResponse.from(memberRepository.save(createMember(pwd, request)));
    }

    private void validateDuplicateEmail(MemberCreateRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    private void validateDuplicatePhone(MemberCreateRequest request) {
        if (memberRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_PHONE);
        }
    }

    private void validateDuplicateUsername(MemberCreateRequest request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_MEMBER_ID);
        }
    }

    public MemberResponse findByUsername(String username) {
        Member member = searchMember(username);
        return MemberResponse.from(member);
    }

    public Page<MemberResponse> findAll(Pageable pageable) {
        return memberRepository.findAllByOrderByCreateTimeDesc(pageable).map(MemberResponse::from);
    }

    @Transactional
    public MemberResponse updateInfo(String username, MemberUpdateRequest request) {
        String encodingPassword = encodePassword(request);
        Member member = searchMember(username);

        Member updated = member.update(encodingPassword, request);
        return MemberResponse.from(updated);
    }

    private String encodePassword(MemberUpdateRequest request) {
        String encodingPassword = request.getPassword();
        if (encodingPassword != null && !encodingPassword.isEmpty()) {
            encodePassword(request.getPassword());
        }
        return encodingPassword;
    }

    private Member searchMember(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public void deleteMemberByUsername(String username) {
        Member member = searchMember(username);
        member.softDelete();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
