package com.dalcho.adme.service;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.dto.RegistryDto;
import com.dalcho.adme.repository.RegistryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith( SpringExtension. class )
@SpringBootTest ( webEnvironment = SpringBootTest . WebEnvironment . RANDOM_PORT )
@Transactional
class RegistryServiceTest {
    @Autowired RegistryRepository registryRepository;
    @Autowired RegistryService registryService;

    @Test
    void register() throws Exception { // 검증 x
        //given
        RegistryDto registry1 = new RegistryDto();
        registry1.setTitle("첫 번째");
        registry1.setMain("1");

        RegistryDto registry2 = new RegistryDto();
        registry2.setTitle("두 번째");
        registry2.setMain("2");

        //when
        Registry saveRegistry1 = registryService.setUpload(registry1);
        Registry saveRegistry2 = registryService.setUpload(registry2);

        //then
        Assertions.assertThat(registry1.getTitle()).isEqualTo(saveRegistry1.getTitle());
        Assertions.assertThat(registry2.getTitle()).isEqualTo(saveRegistry2.getTitle());
    }
}