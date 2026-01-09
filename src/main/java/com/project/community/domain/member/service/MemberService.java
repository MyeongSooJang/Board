package com.project.community.domain.member.service;

import static com.project.community.domain.member.entity.Member.createMember;

import com.project.community.domain.member.dto.MemberCreateRequestDTO;
import com.project.community.domain.member.dto.MemberResponseDTO;
import com.project.community.domain.member.dto.MemberUpdateRequestDTO;
import com.project.community.domain.member.entity.Member;
import com.project.community.domain.member.repository.MemberRepository;
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
        String pwd = encodePassword(request.getMemberPwd());
        return MemberResponseDTO.from(memberRepository.save(createMember(pwd,request)));
    }

    public MemberResponseDTO findByMemberNo(Long memberNo) {
        return MemberResponseDTO.from(searchByMemberNo(memberNo));
    }

    public MemberResponseDTO findByMemberId(String memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 아이디의 회원이 존재하지 않습니다"));
        return MemberResponseDTO.from(member);
    }

    public Page<MemberResponseDTO> findAll(Pageable pageable){
        return memberRepository.findAllOrderByCreateTime(pageable).map(MemberResponseDTO::from);
    }

    @Transactional
    public MemberResponseDTO updateMember (Long memberNo, MemberUpdateRequestDTO request) {
        String encodingPassword = encodePassword(request.getMemberPwd());
        Member updated = searchByMemberNo(memberNo).updateMember(encodingPassword, request);
        return MemberResponseDTO.from(updated);
    }

    @Transactional
    public void deleteMember (Long memberNo) {
        memberRepository.delete(searchByMemberNo(memberNo));
    }

    private Member searchByMemberNo(Long memberNo) {
        return memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new NoSuchElementException("해당하는 번호의 회원이 존재하지 않습니다"));
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
