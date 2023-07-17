package com.dalcho.adme.controller;

import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.PagingDto;
import com.dalcho.adme.dto.registry.RegistryRequestDto;
import com.dalcho.adme.dto.registry.RegistryResponseDto;
import com.dalcho.adme.service.RegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class RegistryController {
    private final RegistryService registryService;

    // 게시글 등록
    @PostMapping("/registry") //@RequestParam이 여러개 있다. -> @ModelAttribute
    public RegistryResponseDto postUpload(@ModelAttribute RegistryRequestDto registryDto, @AuthenticationPrincipal User user) throws IOException {
        return registryService.postUpload(registryDto, user);
    }


    // 작성 글 페이징
    @GetMapping("/space/{curPage}")
    public PagingDto readBoard(@PathVariable Integer curPage) {
        return registryService.getBoards(curPage);
    }


    // 게시글 상세 보기
    @GetMapping("/registry")
    public RegistryResponseDto getIdxRegistry(@RequestParam Long idx) {
        return registryService.getIdxRegistry(idx);
    }

    //mypage 게시글 paging
    @GetMapping("/mypage/{curPage}")
    public PagingDto myPage(@PathVariable int curPage, @AuthenticationPrincipal User user){
        return registryService.myPage(curPage, user);
    }

    // 게시글 삭제
    @DeleteMapping("/registry/{registryId}")
    public void deleteRegistry(@PathVariable Long registryId, @AuthenticationPrincipal User user){
        registryService.deleteRegistry(registryId, user);
    }
}
