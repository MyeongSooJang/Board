package com.project.community.member.controller;

import com.project.community.member.dto.MemberCreateRequest;
import com.project.community.member.dto.MemberResponse;
import com.project.community.member.dto.MemberUpdateRequest;
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
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberCreateRequest request) {
        return ResponseEntity.ok(memberService.enrollMember(request));
    }

    @GetMapping("/{username}")
    public ResponseEntity<MemberResponse> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(memberService.findByUsername(username));
    }

    @GetMapping
    public ResponseEntity<Page<MemberResponse>> findAllMembers(Pageable pageable) {
        return ResponseEntity.ok(memberService.findAll(pageable));
    }

    @PutMapping("/{username}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable String username,
                                                       @Valid @RequestBody MemberUpdateRequest request) {
        return ResponseEntity.ok(memberService.updateInfo(username, request));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteMember(@PathVariable String username) {
        memberService.deleteMemberByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
