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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponseDTO enrollMember(MemberCreateRequestDTO request) {
        return MemberResponseDTO.from(memberRepository.save(createMember(request)));
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
        return memberRepository.findAll(pageable).map(MemberResponseDTO::from);
    }

    public MemberResponseDTO updateMember (Long memberNo, MemberUpdateRequestDTO request) {
        Member updated = searchByMemberNo(memberNo).updateMember(request);
        return MemberResponseDTO.from(updated);
    }

    public void deleteMember (Long memberNo) {
        Member member = searchByMemberNo(memberNo);
        memberRepository.delete(member);
    }

    private Member searchByMemberNo(Long memberNo) {
        return memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new NoSuchElementException("해당하는 번호의 회원이 존재하지 않습니다"));
    }
}
