package com.project.community.boardfile.service;

import com.project.community.boardfile.repository.BoardFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardFileService {
    private final BoardFileRepository boardFileRepository;


}
