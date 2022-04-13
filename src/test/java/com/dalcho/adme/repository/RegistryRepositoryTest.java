package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Registry;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ExtendWith( SpringExtension. class )
@SpringBootTest ( webEnvironment = SpringBootTest . WebEnvironment . RANDOM_PORT )
@Transactional
@Rollback(false)
class RegistryRepositoryTest { // 검증 완료 o
    @Autowired RegistryRepository registryRepository;

    @Test
    void registryTest() throws Exception {
        //given
        Registry registry = new Registry();
        registry.setTitle("hi");
        registry.setMain("hello");

        //when
        registryRepository.save(registry);

        //then
        Assertions.assertThat(1L).isEqualTo(registry.getIdx());
        //Assertions.assertThat("hi").isEqualTo(registry.getTitle());
        //Assertions.assertThat("hello").isEqualTo(registry.getMain());
    }
}