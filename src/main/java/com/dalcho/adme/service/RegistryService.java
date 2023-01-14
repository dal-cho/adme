package com.dalcho.adme.service;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.registry.RegistryRequestDto;
import com.dalcho.adme.dto.registry.RegistryResponseDto;
import com.dalcho.adme.dto.registry.PagingResult;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public interface RegistryService {

    Registry postUpload(RegistryRequestDto registryDto, User user) throws IOException;

    // 작성 글 페이징
    PagingResult getBoards(int curPage);

    // 게시글 상세 보기
    RegistryResponseDto getIdxRegistry(Long idx);

}
