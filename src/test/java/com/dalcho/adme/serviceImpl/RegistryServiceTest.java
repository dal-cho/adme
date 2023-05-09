package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.UserRole;
import com.dalcho.adme.dto.registry.RegistryRequestDto;
import com.dalcho.adme.dto.registry.RegistryResponseDto;
import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.notfound.RegistryNotFoundException;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.service.Impl.RegistryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dalcho.adme.exception.ErrorCode.REGISTRY_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistryServiceTest {
    @Mock
    RegistryRepository registryRepository;
    @InjectMocks
    RegistryServiceImpl registryService;

    User user;
    Registry saveRegistry;
    RegistryRequestDto registryDto;

    @Test
    @BeforeEach
    void settingTest(){
        UserRole role = UserRole.of(UserRole.USER.name());
        user = User.builder()
                .username("username")
                .nickname("nickname")
                .password("password")
                .email("email@naver.com")
                .role(role)
                .build();

        registryDto = new RegistryRequestDto();
        registryDto.setTitle("첫 번째");
        registryDto.setMain("1");
    }


    @Test
    @DisplayName("게시글 저장")
    void post_Registry() throws Exception {
        //given
        saveRegistry = registryDto.toEntity(user);
        when(registryRepository.save(any(Registry.class))).thenReturn(saveRegistry);

        //when
        RegistryResponseDto registry = registryService.postUpload(registryDto, user);
        verify(registryRepository).save(any(Registry.class));

        //then
        assertThat(registryDto.getTitle()).isEqualTo(registry.getTitle());
        assertThat(registryDto.getMain()).isEqualTo(registry.getMain());

    }

    @Test
    @DisplayName("게시글 조회")
    void getRegistry() throws Exception {
        // given
        post_Registry();
        when(registryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(saveRegistry));

        // when
        RegistryResponseDto idxRegistry = registryService.getIdxRegistry(1L);

        // then
        assertEquals(registryDto.getMain(), idxRegistry.getMain());
        assertEquals(registryDto.getTitle(), idxRegistry.getTitle());
    }

    @Test
    @DisplayName("조회 실패")
    void get_registry_not_found(){
        when(registryRepository.findById(anyLong())).thenReturn(Optional.empty());;
        CustomException e = assertThrows(RegistryNotFoundException.class, () ->
                registryService.getIdxRegistry(1L));
        assertEquals(REGISTRY_NOT_FOUND, e.getErrorCode());
    }
}