package com.project.community.domain.board.service;

import com.project.community.domain.board.dto.BoardResponseDTO;
import com.project.community.domain.board.entity.Board;
import com.project.community.domain.board.repository.BoardRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public List<BoardResponseDTO> searchAll() {
        List<Board> boards = boardRepository.findAllByOrderByUpdateTimeDesc();
        return boards.stream().map(BoardResponseDTO::from).toList();
    }

    public List<BoardResponseDTO> searchBoardsByBoardTitle(String title) {
        List<Board> boards = boardRepository.findByBoardTitleContaining(title);
        return boards.stream().map(BoardResponseDTO::from).toList();
    }

    public List<BoardResponseDTO> searchBoardsByMemberName(String memberName) {
        List<Board> boards = boardRepository.findByMemberNoMemberName(memberName);
        return boards.stream().map(BoardResponseDTO::from).toList();
    }
}
