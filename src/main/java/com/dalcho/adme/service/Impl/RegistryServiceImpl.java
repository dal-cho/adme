package com.dalcho.adme.service.Impl;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.PagingDto;
import com.dalcho.adme.dto.registry.RegistryRequestDto;
import com.dalcho.adme.dto.registry.RegistryResponseDto;
import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.invalid.InvalidPermissionException;
import com.dalcho.adme.exception.notfound.RegistryNotFoundException;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.repository.VideoRepository;
import com.dalcho.adme.service.RegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RegistryServiceImpl implements RegistryService {
    private final RegistryRepository registryRepository;
    private final VideoRepository videoRepository;
    private static final int PAGE_POST_COUNT = 12; // Registry : 한 페이지에 존재하는 게시글 수
    private static final int MY_PAGE = 4; // MY_PAGE : 한 페이지에 존재하는 게시글 수

    // 게시글 등록
    @Transactional
    public RegistryResponseDto postUpload(RegistryRequestDto registryDto, User user) throws IOException {
        Registry registry = registryDto.toEntity(user);
        registryRepository.save(registry);
        return RegistryResponseDto.of(registry);
    }

    @Override // 작성 글 페이징
    public PagingDto<Registry> getBoards(int curPage) {
        Pageable pageable = PageRequest.of(curPage - 1, PAGE_POST_COUNT);
        Page<Registry> boards = registryRepository.findAllByOrderByCreatedAtDesc(pageable);
        return PagingDto.of(boards);
    }

    @Override // 게시글 상세 보기
    public RegistryResponseDto getIdxRegistry(Long idx) throws CustomException {
        Registry getIdxRegistry = registryRepository.findById(idx).orElseThrow(RegistryNotFoundException::new);
        return RegistryResponseDto.of(getIdxRegistry);
    }

    @Override
    public PagingDto<Object> myPage(int curPage, User user) {
        Pageable registryPageable = PageRequest.of(curPage - 1, MY_PAGE);
        Pageable videoPageable = PageRequest.of(curPage - 1, MY_PAGE*2);

        Page<Registry> registryPage = registryRepository.findByNickname(user.getNickname(), registryPageable);
        Page<VideoFile> videoPage = videoRepository.findByNickname(user.getNickname(), videoPageable);

        List<Registry> registryList = registryPage.getContent();
        List<VideoFile> videoList = videoPage.getContent();

        List<Object> mergeList = new ArrayList<>();
        mergeList.addAll(registryList);
        mergeList.addAll(videoList);

        long total = Math.max(registryPage.getTotalPages(), videoPage.getTotalPages());

        if(registryPage.getTotalPages()>=videoPage.getTotalPages()){
            return PagingDto.of(registryPageable, mergeList, total);
        }
        return PagingDto.of(videoPageable, mergeList, total);
    }

    @Override
    public void deleteRegistry(Long registryId, User user){
        Registry registry = registryRepository.findById(registryId).orElseThrow(RegistryNotFoundException::new);
        if (!user.getNickname().equals(registry.getUser().getNickname())) {
            throw new InvalidPermissionException();
        }
        registryRepository.delete(registry);
    }
}