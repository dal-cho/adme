package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.video.VideoRequestDto;
import com.dalcho.adme.repository.VideoRepository;
import com.dalcho.adme.service.Impl.VideoServiceImpl;
import com.dalcho.adme.system.OSValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserVideoIntergrationTest {
    @Mock
    VideoRepository videoRepository;
    @InjectMocks
    VideoServiceImpl videoService;

    @Mock MultipartFile multipartFile;
    @Mock OSValidator osValidator;

    @Test
    @DisplayName("파일 업로드 test")
    void uploadFileTest() throws Exception{
        // given
        List<String> role = Collections.singletonList("ROLE_USER");
        User user = User.builder()
                .nickname("nickname")
                .password("good night")
                .name("coco")
                .email("email@naver.com")
                .roles(role)
                .build();

        VideoFile videoFile = VideoFile.builder()
                .title("title")
                .content("content")
                .uuid("uuid")
                .uploadPath("uploadPath")
                .videoDate(LocalDateTime.now())
                .build();
        String uuid = UUID.randomUUID().toString();
        String uploadPath = osValidator.checkOs();
        VideoRequestDto videoRequestDto = new VideoRequestDto("title", "content");

        // when
        videoService.uploadFile(user, videoRequestDto, multipartFile);
        VideoFile videoFile1 = videoRequestDto.toEntity(uuid, uploadPath);
        videoFile1.setUser(user);

        when(videoRepository.save(any())).thenReturn(videoFile1);

        verify(videoRepository).save(any());
    }
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
//        VideoResponseDto videoResponseDto = new VideoResponseDto(
//                video,
//                title,
//                comment,
//                tag
//        );
//
//        // when
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            tenSecondsService.createVideo(videoResponseDto, userId);
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
//        signupRequestDto signupRequestDto = new signupRequestDto();
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
//        VideoResponseDto videoResponseDto = new VideoResponseDto(
//                video,
//                title,
//                comment,
//                tag
//        );
//
//        // when
//        TenSeconds tenSeconds = tenSecondsService.createVideo(videoResponseDto, userId);
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
