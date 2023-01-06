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
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagingH2Test {
    @Mock
    RegistryRepository registryRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    RegistryServiceImpl registryService;
    @Mock
    Page<Registry> registryPage;
    int curPage = 1;

    @Test
    @DisplayName("실제 로직이 잘 동작하는지 test")
    public void paging() {
        User user = User.builder()
                .nickname("nickname")
                .password("123456")
                .build();
        Registry registry = Registry.builder()
                .title("test")
                .main("main")
                .user(user)
                .build();

        when(registryRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(registryPage);
        registryService.getBoards(curPage);
        verify(registryRepository).findAllByOrderByCreatedAtDesc(any(Pageable.class));

        Assertions.assertThat(registryPage.getNumber() + 1).isEqualTo(curPage);
        Assertions.assertThat(registryService.getBoards(1).getStartPage()).isEqualTo(1);
        Assertions.assertThat(registryService.getBoards(1).getEndPage()).isEqualTo(0);
        Assertions.assertThat(registryService.getBoards(1).isPrev()).isEqualTo(false);
        Assertions.assertThat(registryService.getBoards(1).isNext()).isEqualTo(false);

    }


    @Test
    @DisplayName("test2")
    public void paging2() {
        int size = 1;
        int startPage;
        int endPage;
        boolean prev;
        boolean next;
        int nowPage;
        int displayPageNum = 1;

        User user = User.builder()
                .name("username")
                .nickname("nickname")
                .password("123456")
                .build();
        Registry registry = Registry.builder()
                .title("test")
                .main("main")
                .user(user)
                .build();
        Registry registry1 = Registry.builder()
                .title("test")
                .main("main")
                .user(user)
                .build();

        when(registryRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class))).thenReturn(registryPage);
        registryService.getBoards(curPage);
        verify(registryRepository).findAllByOrderByCreatedAtDesc(any(Pageable.class));

        nowPage = (registryPage.getNumber() + 1);
        startPage = (((int) Math.ceil(nowPage / (double) displayPageNum)) - 1) * 5 + 1;
        endPage = startPage + 4;
        prev = startPage == 1 ? false : true;
        next = endPage < registryPage.getTotalPages() ? true : false;
        if (endPage >= registryPage.getTotalPages()) {
            endPage = registryPage.getTotalPages();
            next = false;
        }

        Assertions.assertThat(startPage).isEqualTo(1);
        Assertions.assertThat(endPage).isEqualTo(0);
        Assertions.assertThat(prev).isEqualTo(false);
        Assertions.assertThat(next).isEqualTo(false);
    }

}
