package com.project.community.domain.board.service;

import com.project.community.domain.board.dto.BoardCreateRequestDTO;
import com.project.community.domain.board.dto.BoardResponseDTO;
import com.project.community.domain.board.dto.BoardUpdateRequestDTO;
import com.project.community.domain.board.entity.Board;
import com.project.community.domain.board.repository.BoardRepository;
import com.project.community.domain.boardlike.repository.BoardLikeRepository;
import com.project.community.domain.member.entity.Member;
import com.project.community.domain.member.repository.MemberRepository;
import com.project.community.exception.NotFoundException;
import com.project.community.exception.UnauthorizedException;
import com.project.community.exception.dto.ErrorCode;
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
        Page<Board> boards = boardRepository.findAllByDeleteTimeIsNull(pageable);
        return covertToDTOPage(boards);
    }

    public Page<BoardResponseDTO> searchBoardsByBoardTitle(String title ,Pageable pageable) {
        Page<Board> boards = boardRepository.findByBoardTitle(title,pageable);
        return covertToDTOPage(boards);
    }

    private Page<BoardResponseDTO> covertToDTOPage(Page<Board> boards) {
        return boards.map(board -> BoardResponseDTO.from(board,
                boardLikeRepository.countByBoardId(board.getBoardId())));
    }


    public Page<BoardResponseDTO> searchBoardsByMemberName(String memberName ,Pageable pageable) {
        Page<Board> boards = boardRepository.findByDeleteTimeIsNullAndMemberName(memberName,pageable);
        return covertToDTOPage(boards);
    }

    public Page<BoardResponseDTO> searchBoardsByKeyWord(String keyword, Pageable pageable) {
        Page<Board> boards = boardRepository.findByKeywordInTitleOrContent(keyword, pageable);
        return covertToDTOPage(boards);
    }

    @Transactional
    public BoardResponseDTO searchByBoardId(Long boardId) {
        Board board = boardRepository.findByBoardIdAndDeleteTimeIsNull(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        board.increaseBoardViewCount();
        boardRepository.save(board);
        Long likeCount = boardLikeRepository.countByBoardId(boardId);
        return BoardResponseDTO.from(board, likeCount);
    }

    @Transactional
    public Long createBoard(BoardCreateRequestDTO board) {
        Member member = memberRepository.findById(board.getMemberId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        return boardRepository.save(Board.createBoard(board, member)).getBoardId();
    }

    @Transactional
    public BoardResponseDTO updateBoard(Long boardId, BoardUpdateRequestDTO boardUpdateRequestDTO, String username) {
        Board board = boardRepository.findByBoardIdAndDeleteTimeIsNull(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));

        validateRequestUser(board,username);

        Long likeCount = boardLikeRepository.countByBoardId(boardId);
        board.updateBoard(boardUpdateRequestDTO);
        return BoardResponseDTO.from(board, likeCount);
    }

    private void validateRequestUser(Board board, String username) {
        if(board.getMember().getUsername().equals(username)){
            throw new UnauthorizedException(ErrorCode.NOT_AUTHOR);
        }
    }

    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardRepository.findByBoardIdAndDeleteTimeIsNull(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        board.softDelete();
        boardRepository.save(board);
    }

}
