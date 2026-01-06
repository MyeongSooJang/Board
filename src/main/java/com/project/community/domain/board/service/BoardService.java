package com.project.community.domain.board.service;

import com.project.community.domain.board.dto.BoardCreateRequestDTO;
import com.project.community.domain.board.dto.BoardResponseDTO;
import com.project.community.domain.board.dto.BoardUpdateRequestDTO;
import com.project.community.domain.board.entity.Board;
import com.project.community.domain.board.repository.BoardRepository;
import com.project.community.domain.member.entity.Member;
import com.project.community.domain.member.repository.MemberRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public Page<BoardResponseDTO> searchAll(Pageable pageable) {
        Page<Board> boards = boardRepository.findAllByOrderByUpdateTimeDesc(pageable);
        return boards.map(BoardResponseDTO::from);
    }

    public List<BoardResponseDTO> searchBoardsByBoardTitle(String title) {
        List<Board> boards = boardRepository.findByBoardTitleContainingOrderByUpdateTimeDesc(title);
        return converToDTOList(boards);
    }

    public List<BoardResponseDTO> searchBoardsByMemberName(String memberName) {
        List<Board> boards = boardRepository.findByMemberMemberNameOrderByUpdateTimeDesc(memberName);

        return converToDTOList(boards);
    }

    public List<BoardResponseDTO> searchBoardsByKeyWord(String keyword) {
        List<Board> boards = boardRepository.findByKeywordInTitleOrContent(keyword);
        return converToDTOList(boards);
    }

    private static List<BoardResponseDTO> converToDTOList(List<Board> boards) {
        return boards.stream().map(BoardResponseDTO::from).toList();
    }

    public BoardResponseDTO searchByBoardNo(Long boardNo) {
        Board board = boardRepository.findByBoardNo(boardNo);
        validBoard(board);
        return BoardResponseDTO.from(board);
    }

    private static void validBoard(Board board) {
        if (board == null) {
            throw new NoSuchElementException("해당하는 게시글 번호의 게시글이 존재하지 않습니다");
        }
    }

    @Transactional
    public Long createBoard(BoardCreateRequestDTO board) {
        Member member = memberRepository.findByMemberNo(board.getMemberNo())
                .orElseThrow(() -> new NoSuchElementException("회원번호에 해당하는 회원이 존재하지 않습니다"));
        return boardRepository.save(Board.createBoard(board, member)).getBoardNo();
    }

    @Transactional
    public BoardResponseDTO updateBoard(Long boardNo, BoardUpdateRequestDTO boardUpdateRequestDTO) {
        Board board = boardRepository.findByBoardNo(boardNo);
        validBoard(board);
        board.updateBoard(boardUpdateRequestDTO);
        return BoardResponseDTO.from(board);
    }

    @Transactional
    public void deleteBoard(Long boardNo) {
        Board board = boardRepository.findByBoardNo(boardNo);
        validBoard(board);
        boardRepository.delete(board);
    }

}
