package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.service.Impl.RegistryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class RegistryPagingTest {
    // db를 불러와서 curpage가 x일때 보여지는 페이지
    @Mock
    RegistryRepository registryRepository;
    @InjectMocks
    RegistryServiceImpl registryService;

    @DisplayName("MySql을 이용한 TEST")
    @Test
    void paging() throws Exception {
        int curPage = 1;
        int count = registryService.getBoards(curPage).getCount(); // 총 페이지 수
        int start = registryService.getBoards(curPage).getStartPage(); // 시작 번호
        int end = registryService.getBoards(curPage).getEndPage(); // 끝 번호
        boolean prev = registryService.getBoards(curPage).isPrev();
        boolean next = registryService.getBoards(curPage).isNext();
        if(end >= count) {
            end = count;
            next = false;
        }


        if (prev) {
            assertThat(start).isGreaterThan(5);
        }else{
            assertThat(start).isLessThan(6);
        }

        if (next) {
            assertThat(count).isGreaterThan(5);
        } else {
            assertThat(count).isLessThan(6);
        }

    }

}
