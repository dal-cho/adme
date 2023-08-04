package com.dalcho.adme.controller;

import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.UserRole;
import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.PagingDto;
import com.dalcho.adme.dto.video.VideoRequestDto;
import com.dalcho.adme.dto.video.VideoResponseDto;
import com.dalcho.adme.dto.video.VideoResultDto;
import com.dalcho.adme.service.Impl.VideoServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VideoController.class)
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//    @Autowired
//    private UserDetailServiceImpl userDetailService;
    @MockBean
    private VideoServiceImpl videoService;
    @Autowired
    private ObjectMapper mapper;

    VideoFile videoFile;
    User user;
    List<VideoFile> videoList = new ArrayList<>();
    List<User> userList = new ArrayList<>();
    String videoUploadPath;
    String thumbUploadPath;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();

        videoUploadPath = "src/test/java/com/dalcho/adme/testFiles/mp4_1M_1280x720.mp4";
        thumbUploadPath = "src/test/java/com/dalcho/adme/testFiles/404.jpeg";

        user = new User(1L, "userNickname", "password", "userName", UserRole.USER);

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

        for (int i = 0; i < 30; i++) {
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
    @DisplayName("GET_VIDEO_LIST")
    @WithMockUser
    void 게시글_전체_조회_페이징() throws Exception {
        int CUR_PAGE = 1; // 현재 페이지
        int PAGE_POST_COUNT = 5; // 한 페이지에 보여줄 게시글 수

        //given
        Pageable pageable = PageRequest.of(CUR_PAGE-1, PAGE_POST_COUNT);

        Page<VideoFile> page = new PageImpl<>(videoList, pageable, 5); // Page 객체로 읽어온 값

        given(videoService.getList(any(Integer.class))).willReturn(PagingDto.of(page));

        //when
        mockMvc.perform(
                get("/tenSeconds/list/" + CUR_PAGE)) // 경로 입력
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.content").isArray());

        //then
        verify(videoService).getList(CUR_PAGE);
    }

    @Test
    @DisplayName("GET_FILE")
    @WithMockUser
    void 게시글_단건_조회() throws Exception {
        int ID = 3;

        //given
        VideoFile myVideoFile = videoList.get(ID);
        myVideoFile.setUser(userList.get(ID));

        given(videoService.getFile(any(Long.class))).willReturn(VideoResponseDto.toEntity(videoFile));

        //when
        ResultActions resultActions = mockMvc.perform(get("/tenSeconds/video/"+ID));

        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        //then
        verify(videoService).getFile(ID);
    }

    @Test
    @DisplayName("UPLOAD_FILE")
    @WithMockUser
    void 게시글_로그인_업로드() throws Exception{
        User user = new User(5L, "nickname", "password", "username", UserRole.USER);

//        String token = "Authorization";
//        UserDetails userDetails = userDetailService.loadUserByUsername(user.getNickname());
//        given(jwtTokenProvider.validateToken(token)).willReturn(true);
//        given(jwtTokenProvider.getAuthentication(token)).willReturn(new UsernamePasswordAuthenticationToken(userDetails,token, userDetails.getAuthorities()));

        VideoRequestDto videoRequestDto = VideoRequestDto.builder()
                .title("videoRequestTitle")
                .content("videoRequestContent")
                .setTime(10)
                .build();

        VideoResultDto videoResultDto = VideoResultDto.builder()
                .title("videoRequestTitle")
                .build();

        byte[] requestDtoJson = mapper.writeValueAsBytes(videoRequestDto);
        byte[] videoFile = videoUploadPath.getBytes();
        byte[] thumbFile = thumbUploadPath.getBytes();

        MockMultipartFile multipartDtoFile = new MockMultipartFile("sideData", "sideData", "application/json", requestDtoJson);
        MockMultipartFile multipartVideoFile = new MockMultipartFile("videoFile", "mp4_1M_1280x720.mp4", "video/mp4", new FileInputStream(videoUploadPath));
        MockMultipartFile multipartThumbFile = new MockMultipartFile("thumbnail", "404.jpeg", "image/jpeg", new FileInputStream(thumbUploadPath));

        given(videoService.uploadFile(any(User.class), any(VideoRequestDto.class), any(MultipartFile.class), any(MultipartFile.class))).willReturn(videoResultDto);

        //when
        mockMvc.perform(multipart("/tenSeconds/videos")
//                .part(new MockPart("sideData", requestDtoJson))
                .file(multipartDtoFile)
                .file(multipartVideoFile)
                .file(multipartThumbFile)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andDo(print());

        //then
//        verify(videoService).uploadFile(eq(user), refEq(videoRequestDto), eq(multipartVideoFile), eq(multipartThumbFile));
    }

    @Test
    @DisplayName("UPDATE")
    @WithMockUser
    void 게시물_수정() throws Exception {
        //given
        long ID = 1L;
        VideoRequestDto videoRequestDto = VideoRequestDto.builder()
                .title("ModifyTitle")
                .content("ModifyContent")
                .build();

        VideoResultDto videoResultDto = VideoResultDto.builder()
                .title("ModifyTitle")
                .build();

        byte[] requestDtoJson = mapper.writeValueAsBytes(videoRequestDto);

        MockMultipartFile multipartFile = new MockMultipartFile("updateData", "updateData", "application/json", requestDtoJson);
        MockMultipartFile multipartThumbFile = new MockMultipartFile("thumbnail", "404.jpeg", "image/jpeg", new FileInputStream(thumbUploadPath));

//        VideoServiceImpl videoServiceMock = Mockito.mock(VideoServiceImpl.class);

        given(videoService.update(any(Long.class), any(VideoRequestDto.class), any(MultipartFile.class))).willReturn(videoResultDto);

        mockMvc.perform(multipart(HttpMethod.PUT,"/tenSeconds/video/"+ID)
                .file(multipartFile)
                .file(multipartThumbFile)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
        ).andDo(print());

//        verify((videoService).update(ID, videoRequestDto, multipartThumbFile));
    }
}