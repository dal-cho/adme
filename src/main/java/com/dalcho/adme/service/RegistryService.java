package com.dalcho.adme.service;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.dto.RegistryDto;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.utils.PagingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;


@RequiredArgsConstructor
@Service
public class RegistryService {
    private final RegistryRepository registryRepository;

//    private static final int BLOCK_PAGE_NUM_COUNT = 5; // 블럭에 존재하는 페이지 번호 수
    private static final int PAGE_POST_COUNT = 6; // 한 페이지에 존재하는 게시글 수

    @Transactional
    public Registry setUpload(RegistryDto registryDto) throws IOException {
        Registry registry = new Registry(registryDto);
        registryRepository.save(registry);
        return registry;
    }

    // 테스트
    public List<Registry> doTest() {
        List<Registry> allTest = registryRepository.findAll();
        return allTest;
    }


    public PagingResult getBoards(int curPage) {
        Pageable pageable = PageRequest.of(curPage-1, PAGE_POST_COUNT);
        Page<Registry> boards = registryRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<Registry> boardList = boards.getContent();

        return new PagingResult(boardList, boards.getTotalPages());
    }


    // 게시글 상세 보기
    public Registry getIdxRegistry(Long idx) {
        Registry getIdxRegistry = registryRepository.findById(idx).orElseThrow(
                () -> new NullPointerException("해당 게시글 없음")
        );
        return getIdxRegistry;
    }

}
