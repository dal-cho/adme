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
    private static final int BLOCK_PAGE_NUM_COUNT = 6;

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
        Pageable pageable = PageRequest.of(curPage-1, BLOCK_PAGE_NUM_COUNT);
        Page<Registry> boards = registryRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<Registry> boardList = boards.getContent();

        return new PagingResult(boardList, boards.getTotalPages());
    }

}
