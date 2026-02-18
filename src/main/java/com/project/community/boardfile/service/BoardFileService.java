package com.project.community.boardfile.service;

import com.project.community.board.entity.Board;
import com.project.community.board.repository.BoardRepository;
import com.project.community.boardfile.dto.BoardFileResponse;
import com.project.community.boardfile.entity.BoardFile;
import com.project.community.boardfile.repository.BoardFileRepository;
import com.project.community.exception.NotFoundException;
import com.project.community.exception.ValidationException;
import com.project.community.exception.dto.ErrorCode;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BoardFileService {
    private final BoardFileRepository boardFileRepository;
    private final BoardRepository boardRepository;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    /**
     * 프로젝트 루트/uploads 디렉토리에 파일 저장
     * JAR 실행 시에도 일관되게 동작하도록 절대 경로 사용
     */
    private static String getUploadDir() {
        String uploadDir = System.getProperty("user.dir") + "/uploads";
        return uploadDir;
    }

    /**
     * 게시글에 파일 업로드
     * 1. 파일 검증
     * 2. 디스크에 저장
     * 3. DB에 저장
     */
    @Transactional
    public BoardFileResponse upload(Long boardId, MultipartFile file) throws IOException {
        Board board = searchBoard(boardId);
        validateFile(file);

        String savedName = saveFileLocally(file);

        return saveFile(board, file, savedName);
    }

    private Board searchBoard(Long boardId) {
        return boardRepository.findByBoardIdAndDeleteTimeIsNull(boardId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));
    }

    /**
     * 파일 검증 (크기, 형식)
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException(ErrorCode.EMPTY_FILE);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ValidationException(ErrorCode.FILE_SIZE_EXCEEDED);
        }

        String contentType = file.getContentType();
        if (contentType == null || !isValidImageType(contentType)) {
            throw new ValidationException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

    /**
     * 이미지 파일 형식 확인
     */
    private boolean isValidImageType(String contentType) {
        return contentType.startsWith("image/");
    }

    /**
     * 파일을 디스크에 저장하고 저장된 파일명 반환
     */
    private String saveFileLocally(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(getUploadDir());
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // UUID + 확장자로 고유한 파일명 생성
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String savedFilename = UUID.randomUUID().toString() + "." + fileExtension;

        // 디스크에 저장
        Path filePath = uploadPath.resolve(savedFilename);
        Files.write(filePath, file.getBytes());

        return savedFilename;
    }

    /**
     * 파일 확장자 추출
     */
    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "png";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * BoardFile을 DB에 저장하고 응답 반환
     */
    private BoardFileResponse saveFile(Board board, MultipartFile file, String savedName) {
        String fileUrl = "/uploads/" + savedName;

        BoardFile boardFile = new BoardFile(
                null, // fileId는 자동 생성
                board,
                file.getOriginalFilename(),
                savedName,
                file.getSize(),
                fileUrl,
                LocalDateTime.now()
        );

        BoardFile saved = boardFileRepository.save(boardFile);
        return new BoardFileResponse(saved.getFileId(), saved.getUrl(), saved.getUploadTime());
    }

    /**
     * 게시글의 모든 파일 조회
     */
    public List<BoardFileResponse> findFiles(Long boardId) {
        List<BoardFile> files = boardFileRepository.findAllByBoard_BoardId(boardId);
        return files.stream()
                .map(f -> new BoardFileResponse(f.getFileId(), f.getUrl(), f.getUploadTime()))
                .toList();
    }

    /**
     * 파일 삭제 (디스크 + DB)
     */
    @Transactional
    public void deleteFile(Long fileId) throws IOException {
        BoardFile boardFile = boardFileRepository.findById(fileId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.FILE_NOT_FOUND));

        // 디스크에서 파일 삭제
        deleteFileFromDisk(boardFile.getSavedName());

        // DB에서 삭제
        boardFileRepository.deleteById(fileId);
    }

    /**
     * 게시글의 모든 파일 삭제
     */
    @Transactional
    public void deleteFilesByBoardId(Long boardId) throws IOException {
        List<BoardFile> files = boardFileRepository.findAllByBoard_BoardId(boardId);
        for (BoardFile file : files) {
            deleteFile(file.getFileId());
        }
    }

    /**
     * 디스크에서 파일 삭제
     */
    private void deleteFileFromDisk(String savedName) throws IOException {
        Path filePath = Paths.get(getUploadDir(), savedName);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }
}
