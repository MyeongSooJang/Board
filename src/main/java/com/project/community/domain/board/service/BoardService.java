package com.project.community.domain.board.service;

import com.project.community.domain.board.dto.BoardCreateRequestDTO;
import com.project.community.domain.board.dto.BoardResponseDTO;
import com.project.community.domain.board.dto.BoardUpdateRequestDTO;
import com.project.community.domain.board.entity.Board;
import com.project.community.domain.board.repository.BoardRepository;
import com.project.community.domain.boardlike.repository.BoardLikeRepository;
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
    private final BoardLikeRepository boardLikeRepository;

    public Page<BoardResponseDTO> searchAll(Pageable pageable) {
        Page<Board> boards = boardRepository.findAllByOrderByUpdateTimeDesc(pageable);
        return boards.map(board -> BoardResponseDTO.from(board,
                boardLikeRepository.countByBoardId(board.getBoardId())));
    }

    public List<BoardResponseDTO> searchBoardsByBoardTitle(String title) {
        List<Board> boards = boardRepository.findByBoardTitleContainingOrderByUpdateTimeDesc(title);
        return covertToDTOList(boards);
    }

    private List<BoardResponseDTO> covertToDTOList(List<Board> boards) {
        return boards.stream()
                .map(board -> BoardResponseDTO.from(board, boardLikeRepository.countByBoardId(board.getBoardId())))
                .toList();
    }

    public List<BoardResponseDTO> searchBoardsByMemberName(String memberName) {
        List<Board> boards = boardRepository.findByMemberNameOrderByUpdateTimeDesc(memberName);
        return covertToDTOList(boards);
    }

    public List<BoardResponseDTO> searchBoardsByKeyWord(String keyword) {
        List<Board> boards = boardRepository.findByKeywordInTitleOrContent(keyword);
        return covertToDTOList(boards);
    }

    @Transactional
    public BoardResponseDTO searchByBoardId(Long boardId) {
        Board board = boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시글 번호의 게시글이 존재하지 않습니다"));
        board.increaseBoardViewCount();
        boardRepository.save(board);
        Long likeCount = boardLikeRepository.countByBoardId(boardId);
        return BoardResponseDTO.from(board, likeCount);
    }

    @Transactional
    public Long createBoard(BoardCreateRequestDTO board) {
        Member member = memberRepository.findById(board.getMemberId())
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원이 존재하지 않습니다"));
        return boardRepository.save(Board.createBoard(board, member)).getBoardId();
    }

    @Transactional
    public BoardResponseDTO updateBoard(Long boardId, BoardUpdateRequestDTO boardUpdateRequestDTO) {
        Board board = boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시글 번호의 게시글이 존재하지 않습니다"));
        Long likeCount = boardLikeRepository.countByBoardId(boardId);
        board.updateBoard(boardUpdateRequestDTO);
        return BoardResponseDTO.from(board, likeCount);
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 게시글 번호의 게시글이 존재하지 않습니다"));
        boardRepository.delete(board);
    }

}
