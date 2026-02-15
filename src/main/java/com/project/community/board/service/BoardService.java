package com.project.community.board.service;

import com.project.community.board.dto.BoardCreateRequest;
import com.project.community.board.dto.BoardResponse;
import com.project.community.board.dto.BoardSearchRequest;
import com.project.community.board.dto.BoardUpdateRequest;
import com.project.community.board.dto.SearchType;
import com.project.community.board.dto.SortType;
import com.project.community.board.entity.Board;
import com.project.community.board.repository.BoardRepository;
import com.project.community.boardlike.repository.BoardLikeRepository;
import com.project.community.comment.repository.CommentRepository;
import com.project.community.member.entity.Member;
import com.project.community.member.repository.MemberRepository;
import com.project.community.exception.NotFoundException;
import com.project.community.exception.UnauthorizedException;
import com.project.community.exception.dto.ErrorCode;
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
    private final CommentRepository commentRepository;

    public Page<BoardResponse> searchAll(String sort, Pageable pageable) {
        SortType sortType = SortType.fromSort(sort);
        Page<Board> boards = switch (sortType) {
            case SortType.LATEST -> boardRepository.findLatestBoards(pageable);
            case OLDEST -> boardRepository.findOldestBoards(pageable);
            case HOT -> boardRepository.findHotBoards(pageable);
            case VIEWCOUNT -> boardRepository.findByViewCount(pageable);
            case LIKECOUNT -> boardRepository.findByLikeCount(pageable);
            case COMMENTCOUNT -> boardRepository.findByCommentCount(pageable);
        };
        return covertToDTOPage(boards);
    }


    public Page<BoardResponse> searchBoard(BoardSearchRequest request, Pageable pageable) {
        Page<Board> boards =
                switch (request.getSearchType()) {
                    case SearchType.TITLE -> boardRepository.findByBoardTitle(request.getTitle(), pageable);
                    case SearchType.CONTENT -> boardRepository.findByBoardContent(request.getKeyword(), pageable);
                    case SearchType.TITLE_AND_CONTENT -> boardRepository.findByKeyWord(request.getKeyword(), pageable);
                    case SearchType.WRITER -> boardRepository.findByWriter(request.getWriter(), pageable);
                };
        return covertToDTOPage(boards);
    }

    private Page<BoardResponse> covertToDTOPage(Page<Board> boards) {
        return boards.map(BoardResponse::from);
    }

    @Transactional
    public BoardResponse searchByBoardId(Long boardId) {
        Board board = boardRepository.findByBoardIdAndDeleteTimeIsNull(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        board.increaseViewCount();
        boardRepository.save(board);
        return BoardResponse.from(board);
    }

    @Transactional
    public Long createBoard(BoardCreateRequest request, String username) {
        Member member = searchMember(username);
        return boardRepository.save(Board.createBoard(request.getBoardTitle(),request.getBoardTitle(),member)).getBoardId();
    }

    private Member searchMember(String username) {
        return memberRepository.findByUsernameAndDeleteTimeIsNull(username)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardUpdateRequest boardUpdateRequest, String username) {
        Board board = boardRepository.findByBoardIdAndDeleteTimeIsNull(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));

        validateRequestUser(board, username);

        board.updateBoard(boardUpdateRequest);
        return BoardResponse.from(board);
    }

    private void validateRequestUser(Board board, String username) {
        if (!board.getMember().getUsername().equals(username)) {
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
