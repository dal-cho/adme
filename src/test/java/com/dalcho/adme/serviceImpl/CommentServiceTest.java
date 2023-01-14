package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.comment.CommentRequestDto;
import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.notfound.UserNotFoundException;
import com.dalcho.adme.repository.CommentRepository;
import com.dalcho.adme.repository.RegistryRepository;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.service.Impl.CommentServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.dalcho.adme.exception.ErrorCode.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
    User user;
    CommentRequestDto commentDto;
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

        //given
        Registry registry = Registry.builder()
                .title("안녕하세요")
                .main("hi")
                .user(user)
                .build();

        CommentRequestDto commentDto = new CommentRequestDto();
        commentDto.setComment("funfun");
        commentDto.setNickname("hh");
        commentDto.setRegistryIdx(1L);
        Comment comment = commentDto.toEntity(registry, user);

        //when
        when(userRepository.findByNickname(anyString())).thenReturn(Optional.ofNullable(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        Comment saveComment = commentService.postComment(commentDto);
        verify(commentRepository).save(any(Comment.class));

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

        // 게시글
        registry = new Registry("타이틀", "본문", user);

        // 댓글
        this.commentDto = new CommentRequestDto();
        this.commentDto.setComment("comment");
        this.commentDto.setNickname((user.getNickname()));
        this.commentDto.setRegistryIdx(1L);
    }


    @Test
    @DisplayName("comment 저장")
    void saveComment() throws IOException {
        // given
        Comment save = commentDto.toEntity(registry, user);
        when(userRepository.findByNickname(commentDto.getNickname())).thenReturn(Optional.ofNullable(user));

        // when
        when(commentRepository.save(any(Comment.class))).thenReturn(save);
        Comment comment = commentService.postComment(commentDto);
        verify(commentRepository).save(any(Comment.class));

        // then
        assertEquals(commentDto.getComment(), comment.getComment()); // Comment의 comment가 일치하는지 확인
    }


    @Test
    @DisplayName("comment 수정")
    void updateComment() throws IOException {
        //given
        saveComment();
        Comment comment = new Comment(commentDto.getComment(), registry, user);
        when(registryRepository.findById(any())).thenReturn(Optional.ofNullable(registry));
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        // when
        CommentRequestDto commentDtoEdit = new CommentRequestDto();
        commentDtoEdit.setComment("comment-edit");

        Comment saveComment = commentService.updateComment(1L,commentDtoEdit, user);
        comment.updateComment(commentDtoEdit.getComment());
        verify(commentRepository).save(saveComment);

        //then
        assertEquals(saveComment.getIdx(), comment.getIdx());
        assertEquals(commentDtoEdit.getComment(), comment.getComment()); // Comment 내용이 업데이트 되었는지 확인
    }


    @Test
    @DisplayName("comment 삭제 성공")
    void deleteComment() throws IOException {
        // given
        saveComment();
        Comment comment = new Comment(commentDto.getComment(), registry, user);
        when(registryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(registry));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));

        //when
        commentService.deleteComment(1L, commentDto, user);

        // then
        verify(commentRepository).delete(comment);
    }
}
