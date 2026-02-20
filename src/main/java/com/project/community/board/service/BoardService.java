package com.project.community.board.service;

import com.project.community.board.dto.BoardCreateRequest;
import com.project.community.board.dto.BoardResponse;
import com.project.community.board.dto.BoardSearchRequest;
import com.project.community.board.dto.BoardUpdateRequest;
import com.project.community.board.dto.DeletedBoardSortType;
import com.project.community.board.dto.SearchType;
import com.project.community.board.dto.SortType;
import com.project.community.board.entity.Board;
import com.project.community.board.repository.BoardRepository;
import com.project.community.boardlike.repository.BoardLikeRepository;
import com.project.community.comment.repository.CommentRepository;
import com.project.community.member.entity.Member;
import com.project.community.member.repository.MemberRepository;
import com.project.community.report.repository.ReportRepository;
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
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;

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


    /**
     * 활성 게시글 조회 (일반 사용자용)
     * deleteTime IS NULL인 게시글만 조회 가능
     */
    @Transactional(readOnly = true)
    public BoardResponse searchByBoardId(Long boardId) {
        Board board = boardRepository.findByBoardIdAndDeleteTimeIsNull(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        return BoardResponse.from(board);
    }

    /**
     * 모든 게시글 조회 (관리자용)
     * deleteTime 무관하게 모든 게시글 조회 가능 (삭제된 게시글 포함)
     */
    @Transactional(readOnly = true)
    public BoardResponse searchByBoardIdAsAdmin(Long boardId) {
        Board board = boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        BoardResponse response = BoardResponse.from(board);
        if (response.getIsDeleted()) {
            boolean deletedByReport = reportRepository.existsApprovedReportForBoard(boardId);
            response.setDeletedByReport(deletedByReport);
        }

        return response;
    }

    @Transactional
    public void increaseViewCount(Long boardId) {
        Board board = boardRepository.findByBoardIdAndDeleteTimeIsNull(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
        board.increaseViewCount();
        boardRepository.save(board);
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

    public Page<BoardResponse> searchDeletedAll(String sort, Pageable pageable) {
        DeletedBoardSortType sortType = DeletedBoardSortType.fromSort(sort);
        Page<Board> boards = switch (sortType) {
            case LATEST -> boardRepository.findDeletedOrderByDeleteTime(pageable);
            case VIEWCOUNT -> boardRepository.findDeletedOrderByViewCount(pageable);
            case LIKECOUNT -> boardRepository.findDeletedOrderByLikeCount(pageable);
            case COMMENTCOUNT -> boardRepository.findDeletedOrderByCommentCount(pageable);
        };
        return covertToDTOPage(boards);
    }

}
