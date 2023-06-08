package com.dalcho.adme.service.Impl;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.registry.PagingDto;
import com.dalcho.adme.dto.registry.RegistryRequestDto;
import com.dalcho.adme.dto.registry.RegistryResponseDto;
import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.notfound.RegistryNotFoundException;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.service.RegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RegistryServiceImpl implements RegistryService {
    private final RegistryRepository registryRepository;
    private final UserRepository userRepository;
    private static final int PAGE_POST_COUNT = 12; // 한 페이지에 존재하는 게시글 수
    private static final int MY_PAGE = 4; // 한 페이지에 존재하는 게시글 수
    private int displayPageNum = 5;

    // 게시글 등록
    @Transactional
    public RegistryResponseDto postUpload(RegistryRequestDto registryDto, User user) throws IOException {
        Registry registry = registryDto.toEntity(user);
        registryRepository.save(registry);
        return RegistryResponseDto.of(registry);
    }


    // 작성 글 페이징
    public PagingDto getBoards(int curPage) {
        Pageable pageable = PageRequest.of(curPage - 1, PAGE_POST_COUNT);
        Page<Registry> boards = registryRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<RegistryResponseDto> boardList = boards.stream()
                .map(board -> new RegistryResponseDto(board.getIdx(), board.getTitle()))
                .collect(Collectors.toList());
        int startPage = ((int)Math.floor((curPage-1) / (double)displayPageNum)) * displayPageNum + 1; // 시작 페이지 번호
        int endPage = Math.min(startPage + displayPageNum - 1, boards.getTotalPages()); // 끝 페이지 번호
        boolean prev = startPage != 1; // 이전 페이지 여부
        boolean next = endPage < boards.getTotalPages(); // 다음 페이지 여부
        return PagingDto.builder()
                .boardList(boardList)
                .curPage(curPage)
                .startPage(startPage)
                .endPage(endPage)
                .prev(prev)
                .next(next)
                .build();
    }


    // 게시글 상세 보기
    public RegistryResponseDto getIdxRegistry(Long idx) throws CustomException {
        Registry getIdxRegistry = registryRepository.findById(idx).orElseThrow(RegistryNotFoundException::new);
        return RegistryResponseDto.of(getIdxRegistry);
    }

    public PagingDto myPage(int curPage) {
        Pageable pageable = PageRequest.of(curPage - 1, MY_PAGE);
        Page<Registry> boards = registryRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<RegistryResponseDto> boardList = boards.stream()
                .map(board -> new RegistryResponseDto(board.getIdx(), board.getTitle()))
                .collect(Collectors.toList());
        int totalPage = boards.getTotalPages();
        int startPage = ((int)Math.floor((curPage-1) / (double)displayPageNum)) * displayPageNum + 1; // 시작 페이지 번호
        int endPage = Math.min(startPage + displayPageNum - 1, totalPage); // 끝 페이지 번호
        boolean prev = startPage != 1; // 이전 페이지 여부
        boolean next = endPage < boards.getTotalPages(); // 다음 페이지 여부
        return PagingDto.builder()
                .boardList(boardList)
                .totalPage(totalPage)
                .curPage(curPage)
                .startPage(startPage)
                .endPage(endPage)
                .prev(prev)
                .next(next)
                .build();
    }
}
