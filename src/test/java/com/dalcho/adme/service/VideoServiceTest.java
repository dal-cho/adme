package com.dalcho.adme.service;

import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.UserRole;
import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.PagingDto;
import com.dalcho.adme.dto.video.VideoRequestDto;
import com.dalcho.adme.dto.video.VideoResponseDto;
import com.dalcho.adme.dto.video.VideoResultDto;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.repository.VideoRepository;
import com.dalcho.adme.service.Impl.VideoServiceImpl;
import com.dalcho.adme.system.OSValidator;
import com.dalcho.adme.utils.video.VideoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VideoServiceTest {
    @Mock
    private VideoRepository videoRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OSValidator osValidator;
    @Mock
    private VideoUtils videoUtils;
    @Mock
    private ApplicationEventPublisher publisher;
    @Spy // 실제 메소드를 돌린다. verify 시 사용
    @InjectMocks
    private VideoServiceImpl videoService;

    VideoFile videoFile;
    User user;
    List<VideoFile> videoList = new ArrayList<>();
    List<User> userList = new ArrayList<>();
    String videoUploadPath;
    String thumbUploadPath;

    @BeforeEach
    void setUp() {

        videoUploadPath = "src/test/java/com/dalcho/adme/testFiles/mp4_1M_1280x720.mp4";
        thumbUploadPath = "src/test/java/com/dalcho/adme/testFiles/404.jpeg";

        user = new User(1L, "nickname", "password", "userName", UserRole.USER);

        videoFile = VideoFile.builder()
                .id(1L)
                .title("title")
                .content("content")
                .uuid("uuid")
                .uploadPath(videoUploadPath)
                .thumbnailExt("png")
                .videoDate(LocalDateTime.now())
                .build();

        videoFile.setUser(user);

        for (int i = 0; i < 10; i++) {
            VideoFile vf = VideoFile.builder()
                    .id((long) i)
                    .title("title" + i)
                    .content("content" + i)
                    .uuid("uuid" + i)
                    .uploadPath("/upload/path" + i)
                    .thumbnailExt("png")
                    .videoDate(LocalDateTime.now())
                    .build();

            User us = User.builder()
                    .nickname("nickname" + i)
                    .username("username" + i)
                    .email("email" + i)
                    .build();

            videoList.add(vf);
            userList.add(us);
        }
    }

    @Test
    @DisplayName("UPLOAD_FILE")
    void 게시글_업로드() throws Exception {
        VideoRequestDto videoRequestDto = new VideoRequestDto("title", "content", 10);
        MockMultipartFile multipartVideoFile = new MockMultipartFile("videoFile", "mp4_1M_1280x720.mp4", "video/mp4", new FileInputStream(videoUploadPath));
        MockMultipartFile multipartThumbFile = new MockMultipartFile("thumbnail", "404.jpeg", "image/jpeg", new FileInputStream(thumbUploadPath));

        given(videoRepository.save(any(VideoFile.class))).willReturn(videoFile);
        given(userRepository.findByNickname(any(String.class))).willReturn(Optional.of(user));

        VideoResultDto result = videoService.uploadFile(user, videoRequestDto, multipartVideoFile, multipartThumbFile);

        assertEquals(result.getTitle(), videoFile.getTitle());

        verify(videoService).uploadFile(user, videoRequestDto, multipartVideoFile, multipartThumbFile);
    }

    @Test
    @DisplayName("GET_LIST")
    void getList() {
        int CUR_PAGE = 1; // 현재 페이지
        int PAGE_POST_COUNT = 4; // 한 페이지에 보여줄 게시글 수
        int TOTAL = videoList.size();

        Pageable pageable = PageRequest.of(CUR_PAGE-1, PAGE_POST_COUNT);
        Page<VideoFile> page = new PageImpl<>(videoList, pageable, TOTAL); // Page 객체로 읽어온 값

        given(videoRepository.findAll(any(Pageable.class))).willReturn(page);

        PagingDto<VideoFile> result = videoService.getList(CUR_PAGE);

        assertEquals(result.getContent(), videoList);
        assertEquals(result.getCurPage(), CUR_PAGE);
        assertEquals(result.getTotalPage(), 10);
        assertEquals(result.getEndPage(), 5);

        verify(videoService).getList(CUR_PAGE);
    }

    @Test
    @DisplayName("GET_FILE")
    void 단일_게시글_불러오기() throws NullPointerException{
        long VIDEO_ID = 1L;

        given(videoRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(videoFile));

        VideoResponseDto result = videoService.getFile(VIDEO_ID);

        assertEquals(result.getId(), videoFile.getId());
        assertEquals(result.getTitle(), videoFile.getTitle());
        assertEquals(result.getContent(), videoFile.getContent());
        assertEquals(result.getUploadPath(), videoFile.getUploadPath());

        verify(videoService).getFile(VIDEO_ID);
    }

    @Test
    @DisplayName("UPDATE")
    void 게시글_수정() throws Exception {
        long VIDEO_ID = 1L;
        VideoRequestDto videoRequestDto = VideoRequestDto.builder()
                .title("modifyTitle")
                .content("modifyContent")
                .setTime(7)
                .build();
        MockMultipartFile multipartThumbFile = new MockMultipartFile("thumbnail", "404.jpeg", "image/jpeg", new FileInputStream(thumbUploadPath));

        given(videoRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(videoFile));

        VideoResultDto result = videoService.update(VIDEO_ID, videoRequestDto, multipartThumbFile);

        assertEquals(result.getTitle(), "modifyTitle");

        verify(videoService).update(VIDEO_ID, videoRequestDto, multipartThumbFile);
    }

    @Test
    @DisplayName("DELETE")
    void 게시글_삭제() {
        long VIDEO_ID = 1L;
        User user = new User(1L, "nickname", "password", "usernams", UserRole.USER);

        given(videoRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(videoFile));

        videoService.delete(VIDEO_ID, user);

        verify(videoService ).delete(VIDEO_ID, user);
    }
}