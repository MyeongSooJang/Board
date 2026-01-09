package com.project.community.domain.member.controller;

import com.project.community.domain.member.dto.MemberCreateRequestDTO;
import com.project.community.domain.member.dto.MemberResponseDTO;
import com.project.community.domain.member.dto.MemberUpdateRequestDTO;
import com.project.community.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponseDTO> createMember(@Valid @RequestBody MemberCreateRequestDTO request) {
        return ResponseEntity.ok(memberService.enrollMember(request));
    }

    @GetMapping("/{memberNo}")
    public ResponseEntity<MemberResponseDTO> findByMemberNo(@PathVariable Long memberNo) {
        return ResponseEntity.ok(memberService.findByMemberNo(memberNo));
    }

    @GetMapping("/id/{memberId}")
    public ResponseEntity<MemberResponseDTO> findByMemberId(@PathVariable String memberId) {
        return ResponseEntity.ok(memberService.findByMemberId(memberId));
    }

    @GetMapping
    public ResponseEntity<Page<MemberResponseDTO>> findAllMembers(Pageable pageable) {
        return ResponseEntity.ok(memberService.findAll(pageable));
    }

    @PutMapping("/{memberNo}")
    public ResponseEntity<MemberResponseDTO> updateMember(@PathVariable Long memberNo,
                                                          @Valid @RequestBody MemberUpdateRequestDTO request) {
        return ResponseEntity.ok(memberService.updateMember(memberNo, request));
    }

    @DeleteMapping("/{memberNo}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberNo) {
        memberService.deleteMember(memberNo);
        return ResponseEntity.noContent().build();
    }
}
