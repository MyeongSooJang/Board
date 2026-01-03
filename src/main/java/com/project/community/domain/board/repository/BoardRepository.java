package com.project.community.domain.board.repository;

import com.project.community.domain.board.entity.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByBoardTitle(String title);
    List<Board> findByMemberNoMemberName(String memberName);



}
