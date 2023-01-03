package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.CommentDto;
import com.dalcho.adme.repository.CommentRepository;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.service.Impl.CommentServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@DisplayName("H2를 이용한 Comment TEST")
//@TestPropertySource(locations = "/application.properties")
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Transactional
@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    CommentRepository commentRepository;
    @Mock
    RegistryRepository registryRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    CommentServiceImpl commentService;
    OngoingStubbing<User> saveUser;
    User user;
    CommentDto commentDto;
    Registry registry;

    @Test
    @DisplayName("beforeEach 작성 전에 작성한 post test")
    void save1Comment() throws IOException {
        List<String> role = Collections.singletonList("ROLE_USER");
        user = User.builder()
                .name("username")
                .nickname("hh")
                .password("password")
               // .email("email")
                .roles(role)
                .build();

        //saveUser = userRepository.save(user);
        //when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());

        //given
        Registry registry1 = Registry.builder()
                .title("안녕하세요")
                .main("hi")
                .user(user)
                .build();
        Registry saveRegistry = registryRepository.save(registry1);

        CommentDto commentDto = new CommentDto();
        commentDto.setComment("funfun");
        commentDto.setNickname("hh");
        commentDto.setRegistryIdx(saveRegistry.getIdx());

        Registry registry = registryRepository.getReferenceById(commentDto.getRegistryIdx());
        Comment comment = commentDto.toEntity(registry, user);

        //when
        Comment saveComment = commentService.postComment(commentDto);
        when(commentRepository.save(any(Comment.class))).thenReturn(saveComment);
        verify(commentRepository).save(saveComment);

        //then
        Assertions.assertThat(comment.getComment()).isEqualTo(saveComment.getComment());
        Assertions.assertThat(comment.getUser().getNickname()).isEqualTo(saveComment.getUser().getNickname());
    }


    @BeforeEach
    void beforeEach() {
        List<String> role = Collections.singletonList("ROLE_USER");
        user = User.builder()
                .name("username")
                .nickname("nickname")
                .password("password")
//                .email("email")
                .roles(role)
                .build();

        //saveUser = userRepository.save(user);
        //when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());

        // 게시글
        Registry saveRegistry = new Registry("타이틀", "본문", user);
        this.registry = registryRepository.save(saveRegistry);

        // 댓글
        this.commentDto = new CommentDto();
        this.commentDto.setComment("comment");
        this.commentDto.setNickname((user.getNickname()));
        this.commentDto.setRegistryIdx(saveRegistry.getIdx());
    }


    @Test
    void saveComment() throws IOException {
        // given

        // when
        Comment comment = commentService.postComment(commentDto);

        // then
        Comment commentTest = commentRepository.findById(comment.getIdx()).orElseThrow(
                () -> new NullPointerException("comment 생성 x")
        );

        assertEquals("comment의 id값이 일치하는지 확인", comment.getIdx(), commentTest.getIdx());
        assertEquals("comment의 nickname이 일치하는지 확인", comment.getRegistry().getUser().getNickname(), registry.getUser().getNickname());
    }


    @Test
    @DisplayName("comment 수정")
    void updateComment() throws IOException {
        Comment comment = commentService.postComment(commentDto);

        CommentDto commentDtoEdit = new CommentDto();
        commentDto.setComment("comment-edit");


        //when
        Comment commentTest = commentService.updateComment(comment.getIdx(), comment.getRegistry().getIdx(), commentDtoEdit, user);

        //then
        assertEquals("Comment Id 값이 일치하는지 확인.", comment.getIdx(), commentTest.getIdx());
        assertEquals("Comment 내용이 업데이트 되었는지 확인", commentDtoEdit.getComment(), commentTest.getComment());
    }


    @Test
    @DisplayName("comment 삭제 성공")
    void deleteComment() throws IOException {
        // given
        Comment comment = commentService.postComment(commentDto);

        //when
        commentService.deleteComment(comment.getIdx(), comment.getRegistry().getIdx(), commentDto, user);

        // then
        Optional<Comment> commentTest = commentRepository.findById(comment.getIdx());
        if (commentTest.isPresent())
            throw new IllegalArgumentException("Comment 가 정상적으로 삭제되지 않았습니다.");
        else
            assertEquals("Comment 가 비어있다.", Optional.empty(), commentTest);
    }
}
