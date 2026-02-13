package com.project.community.commentlike.service;

import com.project.community.comment.entity.Comment;
import com.project.community.comment.repository.CommentRepository;
import com.project.community.commentlike.dto.CommentLikeResponse;
import com.project.community.commentlike.entity.CommentLike;
import com.project.community.commentlike.repository.CommentLikeRepository;
import com.project.community.exception.NotFoundException;
import com.project.community.exception.dto.ErrorCode;
import com.project.community.member.entity.Member;
import com.project.community.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public CommentLikeResponse getLikeStatus(Long commentId, String username) {
        Member member = searchMember(username);
        Comment comment = searchComment(commentId);

        Optional<CommentLike> existing = commentLikeRepository.findByCommentIdAndMemberId(commentId, member.getMemberId());
        boolean liked = existing.isPresent();
        Long likeCount = comment.getLikeCount();

        return new CommentLikeResponse(comment.getCommentId(), likeCount, liked);
    }

    @Transactional
    public CommentLikeResponse toggleLike(Long commentId, String username) {
        Member member = searchMember(username);
        Comment comment = searchComment(commentId);

        Optional<CommentLike> existing = commentLikeRepository.findByCommentIdAndMemberId(commentId, member.getMemberId());
        boolean liked = updateLikeStatus(existing, member, comment);

        return new CommentLikeResponse(comment.getCommentId(), comment.getLikeCount(), liked);
    }

    private boolean updateLikeStatus(Optional<CommentLike> existing, Member member, Comment comment) {
        if (existing.isPresent()) {
            comment.decreaseLikeCount();
            commentLikeRepository.delete(existing.get());
            return false;
        } else {
            comment.increaseLikeCount();
            commentLikeRepository.save(new CommentLike(null, member, comment));
            return true;
        }
    }

    private Member searchMember(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Comment searchComment(Long commentId) {
        return commentRepository.findByCommentId(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }
}
