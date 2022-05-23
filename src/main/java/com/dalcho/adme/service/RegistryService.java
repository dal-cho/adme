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

    private int displayPageNum = 5; // 화면 하단에 보여지는 페이지 버튼의 수
    private int startPage; //  화면에 보여지는 페이징 5개 중 시작 페이지 번호
    private int endPage; // 화면에 보여지는 페이징 5개 중 마지막 페이지 번호, 끝 페이지 번호
    private boolean prev; // 이전 버튼 생성 여부
    private boolean next; // 다음 버튼 생성 여부
    private int nowPage; // 현재 페이지

    // 게시글 등록
    @Transactional
    public Registry setUpload(RegistryDto registryDto) throws IOException {
        Registry registry = new Registry(registryDto);
        registryRepository.save(registry);
        return registry;
    }


    // 작성 글 페이징
    public PagingResult getBoards(int curPage) {
        Pageable pageable = PageRequest.of(curPage-1, PAGE_POST_COUNT); // 찾을 페이지, 한 페이지의 size

        Page<Registry> boards = registryRepository.findAllByOrderByCreatedAtDesc(pageable);// 생성 날짜 순으로 보여주기
        List<Registry> boardList = boards.getContent(); // 조회된 데이터

        nowPage = (boards.getNumber()+1);
        startPage = (int) Math.ceil( (nowPage-1) / (double) displayPageNum ) *5+1;
        endPage = startPage+4;
        prev = startPage == 1 ? false : true; // 이전 버튼 생성 여부

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
