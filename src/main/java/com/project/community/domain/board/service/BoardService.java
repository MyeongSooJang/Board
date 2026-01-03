package com.project.community.domain.board.service;

import static com.project.community.domain.board.entity.Board.createNewBoard;

import com.project.community.domain.board.dto.BoardCreateRequestDTO;
import com.project.community.domain.board.dto.BoardResponseDTO;
import com.project.community.domain.board.entity.Board;
import com.project.community.domain.board.repository.BoardRepository;
import com.project.community.domain.member.entity.Member;
import com.project.community.domain.member.repository.MemberRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

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

    public List<BoardResponseDTO> searchBoardsByKeyWord(String keyWord) {
        List<Board> boards = boardRepository.searchBoardsByKeyWord(keyWord);
        return boards.stream().map(BoardResponseDTO::from).toList();
    }

    public Long createBoard(BoardCreateRequestDTO board) {
        Member member = memberRepository.findByMemberNo(board.getMemberNo())
                .orElseThrow(() -> new NoSuchElementException("회원번호에 해당하는 회원이 존재하지 않습니다"));
        return boardRepository.save(createNewBoard(board, member)).getBoardNo();
    }
}
