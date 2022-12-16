package com.dalcho.adme.service;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.dto.RegistryDto;
import com.dalcho.adme.dto.response.ResRegistryDto;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.security.UserDetailsImpl;
import com.dalcho.adme.utils.PagingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;


@Service
public interface RegistryService {

    Registry postUpload(RegistryDto registryDto, UserDetailsImpl userDetails) throws IOException;

    // 작성 글 페이징
    PagingResult getBoards(int curPage);

    // 게시글 상세 보기
    List getIdxRegistry(Long idx);

}
