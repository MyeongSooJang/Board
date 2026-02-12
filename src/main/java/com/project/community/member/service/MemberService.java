package com.project.community.member.service;

import static com.project.community.member.entity.Member.createMember;

import com.project.community.member.dto.MemberCreateRequest;
import com.project.community.member.dto.MemberResponse;
import com.project.community.member.dto.MemberUpdateRequest;
import com.project.community.member.entity.Member;
import com.project.community.member.repository.MemberRepository;
import com.project.community.exception.DuplicateException;
import com.project.community.exception.dto.ErrorCode;
import java.util.NoSuchElementException;
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

    public MemberResponse findByMemberId(Long memberId) {
        return MemberResponse.from(searchByMemberId(memberId));
    }

    public MemberResponse findByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("해당하는 아이디의 회원이 존재하지 않습니다"));
        return MemberResponse.from(member);
    }

    public Page<MemberResponse> findAll(Pageable pageable) {
        return memberRepository.findAllByOrderByCreateTimeDesc(pageable).map(MemberResponse::from);
    }

    @Transactional
    public MemberResponse updateMember(Long memberId, MemberUpdateRequest request) {
        String encodingPassword = null;
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            encodingPassword = encodePassword(request.getPassword());
        }
        Member updated = searchByMemberId(memberId).updateMember(encodingPassword, request);
        return MemberResponse.from(updated);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.delete(searchByMemberId(memberId));
    }

    private Member searchByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원이 존재하지 않습니다"));
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
