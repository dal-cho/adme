package com.dalcho.adme.service;

import com.dalcho.adme.dto.PagingDto;
import com.dalcho.adme.dto.registry.RegistryRequestDto;
import com.dalcho.adme.dto.registry.RegistryResponseDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface RegistryService {

    RegistryResponseDto postUpload(RegistryRequestDto registryDto, UserDetails userDetails) throws IOException;

    // 작성 글 페이징
    PagingDto getBoards(int curPage);

    // 게시글 상세 보기
    RegistryResponseDto getIdxRegistry(Long idx);

    RegistryResponseDto updateRegistry(Long id, RegistryRequestDto requestDto, UserDetails userDetails);

    PagingDto myPage(int curPage, UserDetails userDetails);

    void deleteRegistry(Long registryId, UserDetails userDetails);
}
