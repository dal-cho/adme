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
    private static final int PAGE_POST_COUNT = 9; // 한 페이지에 존재하는 게시글 수
    private int displayPageNum = 5;
    private int startPage;
    private int endPage;
    private boolean prev;
    private boolean next;
    private int nowPage;

    // 게시글 등록
    @Transactional
    public Registry setUpload(RegistryDto registryDto) throws IOException {
        Registry registry = new Registry(registryDto);
        registryRepository.save(registry);
        return registry;
    }


    // 작성 글 페이징
    public PagingResult getBoards(int curPage) {
        Pageable pageable = PageRequest.of(curPage-1, PAGE_POST_COUNT);
        Page<Registry> boards = registryRepository.findAllByOrderByCreatedAtDesc(pageable);// 생성 날짜 순으로 보여주기
        List<Registry> boardList = boards.getContent(); // 조회된 데이터
        nowPage = (boards.getNumber()+1);
        startPage = ( ((int)  Math.ceil(nowPage/ (double) displayPageNum )) -1) *5 +1;
        endPage = startPage+4;
        prev = startPage == 1 ? false : true;
        next = endPage < boards.getTotalPages()?  true : false;
        if(endPage >= boards.getTotalPages()) {
            endPage = boards.getTotalPages();
            next = false;
        }

        return new PagingResult(boardList, boards.getTotalPages(), startPage, endPage, prev, next);
    }


    // 게시글 상세 보기
    public Registry getIdxRegistry(Long idx) {
        Registry getIdxRegistry = registryRepository.findById(idx).orElseThrow(
                () -> new NullPointerException("해당 게시글 없음")
        );
        return getIdxRegistry;
    }

}
