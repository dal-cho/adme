package com.dalcho.login.controller;

import com.dalcho.login.domain.Registry;
import com.dalcho.login.dto.RegistryDto;
import com.dalcho.login.service.RegistryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Registry> doTest() {
        return registryService.doTest();
    }

}
