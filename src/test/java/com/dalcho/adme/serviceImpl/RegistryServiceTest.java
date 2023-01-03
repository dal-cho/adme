package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.RegistryDto;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.service.Impl.RegistryServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistryServiceTest {
    @Mock
    RegistryRepository registryRepository;
    @InjectMocks
    RegistryServiceImpl registryService;

    @Test
    void mockito_test(){
        assertThat(registryService).isNotNull();
    }

    @Test
    void register() throws Exception { // 검증 x
        //given
        List<String> role = Collections.singletonList("ROLE_USER");
        User user = User.builder()
                .name("username")
                .nickname("nickname")
                .password("password")
                //.email("email")
                .roles(role)
                .build();

        RegistryDto registryDto = new RegistryDto();
        registryDto.setTitle("첫 번째");
        registryDto.setMain("1");

        Registry saveRegistry = registryDto.toEntity(user);
        when(registryRepository.save(any(Registry.class))).thenReturn(saveRegistry);

        //when
        Registry registry = registryService.postUpload(registryDto, user);
        verify(registryRepository).save(any(Registry.class));

        //then
        Assertions.assertThat(registryDto.getTitle()).isEqualTo(registry.getTitle());
        Assertions.assertThat(registryDto.getMain()).isEqualTo(registry.getMain());

    }
}