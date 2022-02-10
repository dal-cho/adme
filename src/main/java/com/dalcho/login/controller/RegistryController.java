package com.dalcho.login.controller;

import com.dalcho.login.domain.Registry;
import com.dalcho.login.dto.RegistryDto;
import com.dalcho.login.service.RegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class RegistryController {
    private final RegistryService registryService;

    @PostMapping("/registry") //@RequestParam이 여러개 있다. -> @ModelAttribute
    public Registry setUpload(@ModelAttribute RegistryDto registryDto) throws IOException {
        return registryService.setUpload(registryDto);
    }

    // 테스트(리스트 전체 가져오기)
    @GetMapping("/space")
    public Page<Registry> doTest(
            @RequestParam("page") int page,
            @RequestParam("size") int size)
            //@RequestParam("sortBy") String sortBy,
            //@RequestParam("isAsc") boolean isAsc)
    {
        page = page - 1; // 0부터 시작
        return registryService.doTest(page, size);
    }
}
