package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.service.Impl.CommentServiceImpl;
import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.CommentDto;
import com.dalcho.adme.repository.CommentRepository;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.service.RegistryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@DisplayName("H2를 이용한 Comment TEST")
@TestPropertySource(locations = "/application.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class CommentServiceTest {
    @Autowired
    CommentServiceImpl commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    RegistryService registryService;
    @Autowired
    RegistryRepository registryRepository;
    @Autowired
    UserRepository userRepository;

    User user;
    CommentDto commentDto;
    Registry registry;

    @Test
    @DisplayName("beforeEach 작성 전에 작성한 post test")
    void save1Comment() throws IOException {
        User user = User.builder()
                .nickname("hh")
                .password("password")
                .build();

        User saveUser = userRepository.save(user);

        //given
        Registry registry1 = Registry.builder()
                .title("안녕하세요")
                .main("hi")
                .user(saveUser)
                .build();
        Registry saveRegistry1 = registryRepository.save(registry1);

        CommentDto commentDto = new CommentDto();
        commentDto.setComment("funfun");
        commentDto.setNickname("hh");
        commentDto.setRegistryIdx(saveRegistry1.getIdx());

        Registry registry = registryRepository.getReferenceById(commentDto.getRegistryIdx());
        Comment comment = commentDto.toEntity(registry, user);

        //when
        Comment saveComment = commentService.postComment(commentDto);

        //then
        Assertions.assertThat(comment.getComment()).isEqualTo(saveComment.getComment());
        Assertions.assertThat(comment.getUser().getNickname()).isEqualTo(saveComment.getUser().getNickname());
    }


    @BeforeEach
    void beforeEach() {
        User user = User.builder()
                .nickname("nickname")
                .password("password")
                .build();

        User saveUser = userRepository.save(user);

        // 게시글
        Registry saveRegistry = new Registry("타이틀", "본문", saveUser);
        this.registry = registryRepository.save(saveRegistry);

        // 댓글
        this.commentDto = new CommentDto();
        this.commentDto.setComment("comment");
        this.commentDto.setNickname(saveUser.getNickname());
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
