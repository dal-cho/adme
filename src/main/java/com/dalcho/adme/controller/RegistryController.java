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

//   //테스트(리스트 전체 가져오기)
//    @GetMapping("/space")
//    public List<Registry> doTest() {
//        return registryService.doTest();
//    }

    //작성글 조회
    @GetMapping("/space/{curPage}")
    public PagingResult readBoard(@PathVariable Integer curPage) {
        return registryService.getBoards(curPage);
    }

}
