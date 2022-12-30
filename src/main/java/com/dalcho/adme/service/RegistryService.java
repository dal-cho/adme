package com.dalcho.adme.service;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.RegistryDto;
import com.dalcho.adme.utils.PagingResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public interface RegistryService {

    Registry postUpload(RegistryDto registryDto, User user) throws IOException;

    // 작성 글 페이징
    PagingResult getBoards(int curPage);

    // 게시글 상세 보기
    List getIdxRegistry(Long idx);

}
