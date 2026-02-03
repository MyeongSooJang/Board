package com.project.community.domain.boardfile.repository;

import com.project.community.domain.boardfile.entity.BoardFile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
    List<BoardFile> findAllByBoard_BoardId(Long boardId);
    void deleteByBoard_BoardId(Long boardId);
    boolean existsByBoard_BoardId(Long boardId);
}
