package com.dalcho.adme.domain;

import com.dalcho.adme.dto.VideoDto;
import com.dalcho.adme.service.TenSecondsService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // @Order로 순서 정하는 방법
class TenSecondsTest {

    @Autowired
    TenSecondsService tenSecondsService;

    Long userId = 100L;
    TenSeconds createVideo = null;


//    @Test
//    @Order(1)
//    @DisplayName("정상 케이스")
//    void test1() {
//        // give
//        String video = "https://images.mypetlife.co.kr/content/uploads/2018/07/09155937/KakaoTalk_20180720_165306472-610x610.jpg";
//        String title = "강아지 사진";
//        String comment = "곧 동영상파일로 변환해 보겠습니다.";
//        String tag = "#강아지";
//
//        VideoDto videoDto = new VideoDto(
//                video,
//                title,
//                comment,
//                tag
//        );
//
//        // when
//        TenSeconds tenSeconds = new TenSeconds(videoDto, userId);
//
//        // then
//        assertNull(tenSeconds.getId());
//        assertEquals(userId, tenSeconds.getUserId());
//        assertEquals(video, tenSeconds.getVideo());
//        assertEquals(title, tenSeconds.getTitle());
//        assertEquals(comment, tenSeconds.getComment());
//        assertEquals(tag, tenSeconds.getTag());
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("동영상 등록")
//    void test2() throws SQLException {
//        // give
//        String video = "https://images.mypetlife.co.kr/content/uploads/2018/07/09155937/KakaoTalk_20180720_165306472-610x610.jpg";
//        String title = "강아지 사진";
//        String comment = "곧 동영상파일로 변환해 보겠습니다.";
//        String tag = "#강아지";
//
//        VideoDto videoDto = new VideoDto(
//                video,
//                title,
//                comment,
//                tag
//        );
//
//        // when
//        TenSeconds tenSeconds = tenSecondsService.createVideo(videoDto, userId);
//
//        // then
//        assertNotNull(tenSeconds.getId());
//        assertEquals(userId, tenSeconds.getUserId());
//        assertEquals(video, tenSeconds.getVideo());
//        assertEquals(title, tenSeconds.getTitle());
//        assertEquals(comment, tenSeconds.getComment());
//        assertEquals(tag, tenSeconds.getTag());
//        createVideo = tenSeconds;
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("동영상 조회")
//    void test3() throws SQLException {
//        // give
//        // when
//        List<TenSeconds> videoList = tenSecondsService.getVideos(userId);
//
//        // then
//        // 1. 전체 상품에서 테스트에 의해 생성된 상품 찾아오기 (상품 id로 찾음)
//        Long createVideoId = this.createVideo.getId();
//        TenSeconds foundVideo = videoList.stream()
//                        .filter(tenSeconds -> tenSeconds.getId().equals(createVideoId))
//                        .findFirst()
//                        .orElse(null);
//
//        // 2. Order(2) 테스트에의해 생성된 상품과 일치하는지 검증
//        assertNotNull(foundVideo);
//        assertEquals(userId, foundVideo.getUserId());
//        assertEquals(this.createVideo.getId(), foundVideo.getId());
//        assertEquals(this.createVideo.getVideo(), foundVideo.getVideo());
//        assertEquals(this.createVideo.getTitle(), foundVideo.getTitle());
//        assertEquals(this.createVideo.getComment(), foundVideo.getComment());
//        assertEquals(this.createVideo.getTag(), foundVideo.getTag());
//    }
}