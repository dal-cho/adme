package com.dalcho.adme.service;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.CommentDto;
import com.dalcho.adme.dto.RegistryDto;
import com.dalcho.adme.dto.SignupRequestDto;
import com.dalcho.adme.repository.CommentRepository;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.security.UserDetailsImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@DisplayName("H2를 이용한 Comment TEST")
@TestPropertySource(locations = "/application.properties")
@ExtendWith( SpringExtension. class )
@SpringBootTest( webEnvironment = SpringBootTest . WebEnvironment . RANDOM_PORT )
@Transactional
public class CommentServiceTest {
    @Autowired CommentService commentService;
    @Autowired CommentRepository commentRepository;
    @Autowired RegistryService registryService;
    @Autowired UserService userService;
    @Autowired RegistryRepository registryRepository;

    UserDetailsImpl nowUser;
    CommentDto commentDto;
    Registry registry;

    @Test
    @DisplayName("beforeEach 작성 전에 작성한 post test")
    void save1Comment() throws IOException {
        //given
        CommentDto comment = new CommentDto();
        comment.setComment("funfun");
        comment.setNickname("hh");
        comment.setRegistryId(1);
        comment.setRegistryNickname("first");

        //when
        Comment saveComment = commentService.setComment(comment);

        //then
        Assertions.assertThat(comment.getComment()).isEqualTo(saveComment.getComment());
        Assertions.assertThat(comment.getNickname()).isEqualTo(saveComment.getNickname());
        Assertions.assertThat(comment.getRegistryId()).isEqualTo(saveComment.getRegistryId());
    }


    @BeforeEach
    void beforeEach() {
        SignupRequestDto userDto = new SignupRequestDto("test1", "test1", "d","d","d");
        User user = userService.registerUser(userDto);
        this.nowUser = new UserDetailsImpl(user);

        // 게시글
        RegistryDto registryDto = new RegistryDto("test1","타이틀","본문");
        Registry saveRegistry = new Registry(registryDto);
        this.registry = registryRepository.save(saveRegistry);

        // 댓글
        this.commentDto = new CommentDto();
        int registryIdx = Math.toIntExact(registry.getIdx());
        this.commentDto.setComment("comment");
        this.commentDto.setNickname(nowUser.getUsername());
        this.commentDto.setRegistryId(registryIdx);
        this.commentDto.setRegistryNickname(registry.getNickname()); // 작성자
    }


    @Test
    void saveComment() throws IOException {
        // given

        // when
        Comment comment = commentService.setComment(commentDto);

        // then
        Comment commentTest = commentRepository.findById(comment.getIdx()).orElseThrow(
                () -> new NullPointerException("comment 생성 x")
        );

        assertEquals("comment의 id값이 일치하는지 확인", comment.getIdx(), commentTest.getIdx());
        assertEquals("comment의 nickname이 일치하는지 확인", comment.getRegistryNickname(), registry.getNickname());
    }




    @Test
    @DisplayName("comment 수정")
    void updateComment() throws IOException {

        int registryIdx = Math.toIntExact(registry.getIdx());
        Comment comment = commentService.setComment(commentDto);

        CommentDto commentDtoEdit = new CommentDto();
        commentDto.setComment("comment-edit");


        //when
        Comment commentTest = commentService.updateComment(comment.getIdx(), registryIdx, commentDtoEdit, nowUser);

        //then
        assertEquals("Comment Id 값이 일치하는지 확인.", comment.getIdx(), commentTest.getIdx());
        assertEquals("Comment 내용이 업데이트 되었는지 확인", commentDtoEdit.getComment(), commentTest.getComment());
    }



    @Test
    @DisplayName("comment 삭제 성공")
    void deleteComment() throws IOException {
        // given
        Comment comment = commentService.setComment(commentDto);

        //when
        int registryIdx = Math.toIntExact(registry.getIdx());
        commentService.deleteComment(comment.getIdx(), registryIdx, commentDto, nowUser);

        // then
        Optional<Comment> commentTest = commentRepository.findById(comment.getIdx());
        if (commentTest.isPresent())
            throw new IllegalArgumentException("Comment 가 정상적으로 삭제되지 않았습니다.");
        else
            assertEquals("Comment 가 비어있다.", Optional.empty(), commentTest);
    }
}
