package com.project.community.boardfile.repository;

import com.project.community.boardfile.entity.BoardFile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    // 게시글의 모든 파일 조회
    List<BoardFile> findAllByBoard_BoardId(Long boardId);

    // 게시글의 파일 삭제
    void deleteByBoard_BoardId(Long boardId);

    // 게시글의 파일 존재 확인
    boolean existsByBoard_BoardId(Long boardId);
}
