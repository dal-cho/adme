package com.dalcho.login.repository;

import com.dalcho.login.domain.Registry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@SpringBootTest
@Transactional
@Rollback(false)
class RegistryRepositoryTest {
    @Autowired RegistryRepository registryRepository;

    @Test
    void registry등록() throws Exception {
        Registry registry = new Registry();
        registry.setTitle("첫번째 글 작성");
        registry.setMain("내가 첫번째임");

        registryRepository.save(registry);
        System.out.println("registry : " + registry);
    }

    @Test
    void registry읽기() throws Exception {
        Optional<Registry> registry = registryRepository.findById(1L);
        registry.ifPresent(readRegistry -> {
            System.out.println("Registry : " + readRegistry);
            System.out.println("Registry : " + readRegistry.getTitle());
        });
    }

}