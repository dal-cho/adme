package com.dalcho.adme.service;

import com.dalcho.adme.repository.RegistryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;
import java.util.List;
import java.util.Random;

//import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class RegistryPagingTest {
    // db를 불러와서 curpage가 x일때 보여지는 페이지
    @Autowired
    RegistryRepository registryRepository;
    @Autowired
    RegistryService registryService;

    @DisplayName("MySql을 이용한 TEST")
    @Test
    void paging() throws Exception {
        Random random = new Random();
        int count = registryService.getBoards(1).getCount(); // 총 페이지 수
        int randomCount = random.nextInt(count + 1); // 1~count 사이의 랜덤

        int start = registryService.getBoards(randomCount).getStartPage(); // 시작 번호
        int end = registryService.getBoards(randomCount).getEndPage(); // 끝 번호

        boolean prev = registryService.getBoards(randomCount).isPrev();
        boolean next = registryService.getBoards(randomCount).isNext();
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
