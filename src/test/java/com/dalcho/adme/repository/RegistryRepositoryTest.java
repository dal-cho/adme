package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@DisplayName("H2를 이용한 TEST")
@DataJpaTest
class RegistryRepositoryTest { // 검증 완료 o
    @Autowired RegistryRepository registryRepository;
    @Autowired UserRepository userRepository;

    User user;
    @Test
    void registryTest() throws Exception {
        //given
        User saveuser = User.builder()
                .nickname("nickname")
                .password("password")
                .build();

        user = userRepository.save(saveuser);
        Registry registry = Registry.builder()
                .title("hi")
                .main("hello")
                .user(user)
                .build();

        //when
        registryRepository.save(registry);

        //then
        Assertions.assertThat("nickname").isEqualTo(registry.getUser().getNickname());
        Assertions.assertThat("hi").isEqualTo(registry.getTitle());
        Assertions.assertThat("hello").isEqualTo(registry.getMain());
    }
}