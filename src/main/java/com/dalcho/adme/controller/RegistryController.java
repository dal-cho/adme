package com.dalcho.adme.controller;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.dto.RegistryDto;
import com.dalcho.adme.service.RegistryService;
import com.dalcho.adme.utils.PagingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class RegistryController {
    private final RegistryService registryService;

    @PostMapping("/registry") //@RequestParam이 여러개 있다. -> @ModelAttribute
    public Registry setUpload(@ModelAttribute RegistryDto registryDto) throws IOException {
        return registryService.setUpload(registryDto);
    }


    //작성 글 페이징
    @GetMapping("/space/{curPage}")
    public PagingResult readBoard(@PathVariable Integer curPage) {
        return registryService.getBoards(curPage);
    }


    // 게시글 상세 보기
    @GetMapping("/registry")
    public Registry getIdxRegistry(@RequestParam Long idx) {
        return registryService.getIdxRegistry(idx);
    }
}
