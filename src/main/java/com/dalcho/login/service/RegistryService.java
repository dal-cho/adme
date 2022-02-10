package com.dalcho.login.service;

import com.dalcho.login.domain.Registry;
import com.dalcho.login.dto.RegistryDto;
import com.dalcho.login.repository.RegistryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Transactional
    public Registry setUpload(RegistryDto registryDto) throws IOException {
        Registry registry = new Registry(registryDto);
        registryRepository.save(registry);
        return registry;
    }

    // 테스트
    public Page<Registry> doTest(int page, int size) {
//        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Sort sort = Sort.by(direction, sortBy);

        Pageable pageable = PageRequest.of(page, size);
        //Pageable pageable = PageRequest.of(page, size, sort);
        Page<Registry> allTest = registryRepository.findAll(pageable);
        return allTest;
    }
}
