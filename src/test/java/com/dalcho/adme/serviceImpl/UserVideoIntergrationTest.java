package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // @Order로 순서 정하는 방법
public class UserVideoIntergrationTest {

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;


    Long userId = null;

//    @Test
//    @Order(1)
//    @DisplayName("회원가입 정보 없이 동영상 등록 시 에러 발생")
//    void test1() {
//        // given
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
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            tenSecondsService.createVideo(videoDto, userId);
//        });
//
//        //then
//        assertEquals("회원 Id가 유요하지 않습니다.", exception.getMessage());
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("회원 가입")
//    void test2() {
//        // given
//        String username = "pink sweat$";
//        String nickname = "핑크 돼지";
//        String password = "pinkpig";
//        String email = "pinkpig@gmail.com";
//        boolean admin = false;
//
//        SignupRequestDto signupRequestDto = new SignupRequestDto();
//        signupRequestDto.setUsername(username);
//        signupRequestDto.setNickname(nickname);
//        signupRequestDto.setPassword(password);
//        signupRequestDto.setEmail(email);
//        signupRequestDto.setAdmin(admin);
//
//        // when
//        User user = userService.registerUser(signupRequestDto);
//
//        // then
//        assertNotNull(user.getId());
//        assertEquals(username, user.getUsername());
//        assertTrue(passwordEncoder.matches(password, user.getPassword()));
//        assertEquals(email, user.getEmail());
//        assertEquals(UserRole.USER, user.getRole());
//
//        userId = user.getId();
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("가입한 회원 Id 로 신규 동영상 등록")
//    void test3() throws SQLException {
//        // given
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
//    @Order(4)
//    @DisplayName("회원이 등록한 모든 관심상품 조회")
//    void test4() {
//        //given
//        // when
//        List<TenSeconds> videoList = tenSecondsService.getVideos(userId);
//
//        // then
//        // 1. 전체 상품에서 테스트에 의해 생성된 상품 찾아오기 (상품 id로 찾음)
//        Long createVideoId = this.createVideo.getId();
//        TenSeconds foundVideo = videoList.stream()
//                .filter(tenSeconds -> tenSeconds.getId().equals(createVideoId))
//                .findFirst()
//                .orElse(null);
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
