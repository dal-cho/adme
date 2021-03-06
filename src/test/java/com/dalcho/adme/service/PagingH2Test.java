package com.dalcho.adme.service;

import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.repository.RegistryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@DisplayName("H2를 이용한 TEST")
@TestPropertySource(locations = "/application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PagingH2Test {
    @Autowired
    RegistryRepository registryRepository;
    @Autowired
    RegistryService registryService;

    @Test
    @DisplayName("실제 로직이 잘 동작하는지 test")
    public void paging() {
        Registry registry = registryRepository.save(new Registry(1L,"HI","TEST","TEST"));
        Registry registry1 = registryRepository.save(new Registry(2L,"HI","TEST","TEST"));
        Registry registry2 = registryRepository.save(new Registry(3L,"HI","TEST","TEST"));
        Registry registry3 = registryRepository.save(new Registry(4L,"HI","TEST","TEST"));
        Registry registry4 = registryRepository.save(new Registry(5L,"HI","TEST","TEST"));
        Registry registry5 = registryRepository.save(new Registry(6L,"HI","TEST","TEST"));

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
        int displayPageNum=5;

        Registry registry = registryRepository.save(new Registry(1L,"HI","TEST","TEST"));
        Registry registry1 = registryRepository.save(new Registry(2L,"HI","TEST","TEST"));
        Registry registry2 = registryRepository.save(new Registry(3L,"HI","TEST","TEST"));
        Registry registry3 = registryRepository.save(new Registry(4L,"HI","TEST","TEST"));
        Registry registry4 = registryRepository.save(new Registry(5L,"HI","TEST","TEST"));
        Registry registry5 = registryRepository.save(new Registry(6L,"HI","TEST","TEST"));

        Pageable pageable = PageRequest.of(curPage-1, size);
        Page<Registry> boards = registryRepository.findAllByOrderByCreatedAtDesc(pageable);// 생성 날짜 순으로 보여주기

        nowPage = (boards.getNumber()+1);
        startPage = ( ((int)  Math.ceil(nowPage/ (double) displayPageNum )) -1) *5 +1;
        endPage = startPage+4;
        prev = startPage == 1 ? false : true;
        next = endPage < boards.getTotalPages()?  true : false;
        if(endPage >= boards.getTotalPages()) {
            endPage = boards.getTotalPages();
            next = false;
        }

        Assertions.assertThat(startPage).isEqualTo(1);
        Assertions.assertThat(endPage).isEqualTo(5);
        Assertions.assertThat(prev).isEqualTo(false);
        Assertions.assertThat(next).isEqualTo(true);
    }

}
