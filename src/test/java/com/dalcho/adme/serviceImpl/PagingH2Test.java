package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.service.Impl.RegistryServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagingH2Test {
    @Mock
    RegistryRepository registryRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    RegistryServiceImpl registryService;
    int curPage = 1;

    @Test
    @DisplayName("실제 로직이 잘 동작하는지 test")
    public void paging() {
        //given
        User user = User.builder()
                .nickname("nickname")
                .password("123456")
                .build();
        List<Registry> registryList = new ArrayList<>();
        Registry registry = new Registry("title", "main", user);
        Registry registry1 = new Registry("title", "main", user);
        Registry registry2 = new Registry("title", "main", user);
        Registry registry3 = new Registry("title", "main", user);
        Registry registry4 = new Registry("title", "main", user);
        Registry registry5 = new Registry("title", "main", user);
        registryList.add(registry);
        registryList.add(registry1);
        registryList.add(registry2);
        registryList.add(registry3);
        registryList.add(registry4);
        registryList.add(registry5);
        Page<Registry> responsePage = new PageImpl<>(registryList);
        when(registryRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(responsePage);

        // when
        registryService.getBoards(curPage);
        verify(registryRepository).findAllByOrderByCreatedAtDesc(any(Pageable.class));

        // then
        Assertions.assertThat(responsePage.getNumber() + 1).isEqualTo(curPage);
        Assertions.assertThat(registryService.getBoards(1).getStartPage()).isEqualTo(1);
        Assertions.assertThat(registryService.getBoards(1).getEndPage()).isEqualTo(1);
        Assertions.assertThat(registryService.getBoards(1).isPrev()).isEqualTo(false);
        Assertions.assertThat(registryService.getBoards(1).isNext()).isEqualTo(false);

    }
}
