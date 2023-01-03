package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.service.Impl.RegistryServiceImpl;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

@DisplayName("H2를 이용한 TEST")
@TestPropertySource(locations = "/application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PagingH2Test {
    @Mock
    RegistryRepository registryRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    RegistryServiceImpl registryService;

    @Test
    @DisplayName("실제 로직이 잘 동작하는지 test")
    public void paging() {
        User user = User.builder()
                .nickname("nickname")
                .password("123456")
                .build();
        User saveUser = userRepository.save(user);
        Registry registry = registryRepository.save(new Registry("TEST", "HI", saveUser));
        Registry registry1 = registryRepository.save(new Registry("TEST", "HI", saveUser));
        Registry registry2 = registryRepository.save(new Registry("TEST", "HI", saveUser));
        Registry registry3 = registryRepository.save(new Registry("TEST", "HI", saveUser));
        Registry registry4 = registryRepository.save(new Registry("TEST", "HI", saveUser));
        Registry registry5 = registryRepository.save(new Registry("TEST", "HI", saveUser));

        int curPage = 1;
        Pageable pageable = PageRequest.of(curPage - 1, 5);
        Page<Registry> boards = registryRepository.findAllByOrderByCreatedAtDesc(pageable);

        Assertions.assertThat(boards.getNumber() + 1).isEqualTo(curPage);
        Assertions.assertThat(registryService.getBoards(1).getStartPage()).isEqualTo(1);
        Assertions.assertThat(registryService.getBoards(1).getEndPage()).isEqualTo(1);
        Assertions.assertThat(registryService.getBoards(1).isPrev()).isEqualTo(false);
        Assertions.assertThat(registryService.getBoards(1).isNext()).isEqualTo(false);
    }


    @Test
    @DisplayName("size를 바꿔도 실행이 잘 되는지 test")
    public void paging2() {
        int curPage = 1;
        int size = 1;
        int startPage;
        int endPage;
        boolean prev;
        boolean next;
        int nowPage;
        int displayPageNum = 5;

        User user = User.builder()
                .name("username")
                .nickname("nickname")
                .password("123456")
                .build();
        User saveUser = userRepository.save(user);

        Registry registry = registryRepository.save(new Registry("TEST", "HI", saveUser));
        Registry registry1 = registryRepository.save(new Registry("TEST", "HI", saveUser));
        Registry registry2 = registryRepository.save(new Registry("TEST", "HI", saveUser));
        Registry registry3 = registryRepository.save(new Registry("TEST", "HI", saveUser));
        Registry registry4 = registryRepository.save(new Registry("TEST", "HI", saveUser));
        Registry registry5 = registryRepository.save(new Registry("TEST", "HI", saveUser));

        Pageable pageable = PageRequest.of(curPage - 1, size);
        Page<Registry> boards = registryRepository.findAllByOrderByCreatedAtDesc(pageable);// 생성 날짜 순으로 보여주기

        nowPage = (boards.getNumber() + 1);
        startPage = (((int) Math.ceil(nowPage / (double) displayPageNum)) - 1) * 5 + 1;
        endPage = startPage + 4;
        prev = startPage == 1 ? false : true;
        next = endPage < boards.getTotalPages() ? true : false;
        if (endPage >= boards.getTotalPages()) {
            endPage = boards.getTotalPages();
            next = false;
        }

        Assertions.assertThat(startPage).isEqualTo(1);
        Assertions.assertThat(endPage).isEqualTo(5);
        Assertions.assertThat(prev).isEqualTo(false);
        Assertions.assertThat(next).isEqualTo(true);
    }

}
