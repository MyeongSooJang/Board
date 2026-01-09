package com.project.community.domain.member.repository;

import com.project.community.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberNo(Long memberNo);
    Optional<Member> findByMemberId(String memberId);
    Page<Member> findAllOrderByCreateTime(Pageable pageable);
}
