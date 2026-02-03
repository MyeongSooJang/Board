package com.project.community.domain.boardfile.service;

import com.project.community.domain.boardfile.dto.BoardFileRequestDTO;
import com.project.community.domain.boardfile.dto.BoardFileResponse;
import com.project.community.domain.boardfile.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardFileService {
    private final BoardFileRepository boardFileRepository;


}
