package com.project.community.member.controller;

import com.project.community.member.dto.MemberCreateRequestDTO;
import com.project.community.member.dto.MemberResponseDTO;
import com.project.community.member.dto.MemberUpdateRequestDTO;
import com.project.community.member.service.MemberService;
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

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> findByMemberId(@PathVariable Long memberId) {
        return ResponseEntity.ok(memberService.findByMemberId(memberId));
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<MemberResponseDTO> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(memberService.findByUsername(username));
    }

    @GetMapping
    public ResponseEntity<Page<MemberResponseDTO>> findAllMembers(Pageable pageable) {
        return ResponseEntity.ok(memberService.findAll(pageable));
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> updateMember(@PathVariable Long memberId,
                                                          @Valid @RequestBody MemberUpdateRequestDTO request) {
        return ResponseEntity.ok(memberService.updateMember(memberId, request));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.noContent().build();
    }
}
