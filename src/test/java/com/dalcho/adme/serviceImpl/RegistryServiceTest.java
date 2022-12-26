package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.Impl.RegistryServiceImpl;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.RegistryDto;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.security.UserDetailsImpl;
import com.dalcho.adme.service.RegistryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest ( webEnvironment = SpringBootTest . WebEnvironment . RANDOM_PORT )
@Transactional
class RegistryServiceTest {
    @Autowired RegistryRepository registryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RegistryServiceImpl registryService;

    @Test
    void register() throws Exception { // 검증 x
        //given
        User user = User.builder()
                .username("username")
                .nickname("nickname")
                .password("password")
                .email("email")
                .build();

        User saveUser = userRepository.save(user);
        UserDetailsImpl userDetails = new UserDetailsImpl(saveUser);

        RegistryDto registry1 = new RegistryDto();
        registry1.setTitle("첫 번째");
        registry1.setMain("1");

        RegistryDto registry2 = new RegistryDto();
        registry2.setTitle("두 번째");
        registry2.setMain("2");

        //when

        Registry saveRegistry1 = registryService.postUpload(registry1, userDetails);
        Registry saveRegistry2 = registryService.postUpload(registry2, userDetails);

        //then
        Assertions.assertThat(registry1.getTitle()).isEqualTo(saveRegistry1.getTitle());
        Assertions.assertThat(registry2.getTitle()).isEqualTo(saveRegistry2.getTitle());
    }
}