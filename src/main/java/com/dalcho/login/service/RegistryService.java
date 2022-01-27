package com.dalcho.login.service;

import com.dalcho.login.domain.Registry;
import com.dalcho.login.dto.RegistryDto;
import com.dalcho.login.repository.RegistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RegistryService {
    private final RegistryRepository registryRepository;

    @Transactional
    public Registry setUpload(RegistryDto registryDto) throws IOException {
        Registry upload = new Registry(registryDto);
        registryRepository.save(upload);
        return upload;
    }

    // 테스트
    public List<Registry> doTest() {
        List<Registry> allTest = registryRepository.findAll();
        return allTest;
    }

}
