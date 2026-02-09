package com.project.community.member.service;

import static com.project.community.member.entity.Member.createMember;

import com.project.community.member.dto.MemberCreateRequestDTO;
import com.project.community.member.dto.MemberResponseDTO;
import com.project.community.member.dto.MemberUpdateRequestDTO;
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
    public MemberResponseDTO enrollMember(MemberCreateRequestDTO request) {
        validateDuplicateUsername(request);
        validateDuplicatePhone(request);
        validateDuplicateEmail(request);
        String pwd = encodePassword(request.getPassword());
        return MemberResponseDTO.from(memberRepository.save(createMember(pwd, request)));
    }

    private void validateDuplicateEmail(MemberCreateRequestDTO request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    private void validateDuplicatePhone(MemberCreateRequestDTO request) {
        if (memberRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_PHONE);
        }
    }

    private void validateDuplicateUsername(MemberCreateRequestDTO request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateException(ErrorCode.DUPLICATE_MEMBER_ID);
        }
    }

    public MemberResponseDTO findByMemberId(Long memberId) {
        return MemberResponseDTO.from(searchByMemberId(memberId));
    }

    public MemberResponseDTO findByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("해당하는 아이디의 회원이 존재하지 않습니다"));
        return MemberResponseDTO.from(member);
    }

    public Page<MemberResponseDTO> findAll(Pageable pageable) {
        return memberRepository.findAllByOrderByCreateTimeDesc(pageable).map(MemberResponseDTO::from);
    }

    @Transactional
    public MemberResponseDTO updateMember(Long memberId, MemberUpdateRequestDTO request) {
        String encodingPassword = null;
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            encodingPassword = encodePassword(request.getPassword());
        }
        Member updated = searchByMemberId(memberId).updateMember(encodingPassword, request);
        return MemberResponseDTO.from(updated);
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
