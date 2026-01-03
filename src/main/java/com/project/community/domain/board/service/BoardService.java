package com.project.community.domain.board.service;

import com.project.community.domain.board.entity.Board;
import com.project.community.domain.board.repository.BoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<Board> searchBoardsByBoardTitle(String title) {
        return boardRepository.findByBoardTitle(title);
    }

    public List<Board> searchBoardsByMemberName(String memberName) {
        return boardRepository.findByMemberNoMemberName(memberName);
    }
}
