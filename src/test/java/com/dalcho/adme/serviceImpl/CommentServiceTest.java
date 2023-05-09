package com.dalcho.adme.serviceImpl;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.UserRole;
import com.dalcho.adme.dto.comment.CommentRequestDto;
import com.dalcho.adme.dto.comment.CommentResponseDto;
import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.notfound.CommentNotFoundException;
import com.dalcho.adme.exception.notfound.RegistryNotFoundException;
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

import static com.dalcho.adme.exception.ErrorCode.*;
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
        UserRole role = UserRole.valueOf(UserRole.USER.name());
        user = User.builder()
                .username("username")
                .nickname("hh")
                .password("password")
                .email("email@naver.com")
                .role(role)
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
        when(registryRepository.getReferenceById(anyLong())).thenReturn(registry);
        when(userRepository.findByNickname(anyString())).thenReturn(Optional.ofNullable(user));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentResponseDto saveComment = commentService.postComment(commentDto);
        verify(commentRepository).save(any(Comment.class));

        //then
        Assertions.assertThat(comment.getComment()).isEqualTo(saveComment.getComment());
        Assertions.assertThat(comment.getUser().getNickname()).isEqualTo(saveComment.getCommentNickname());
    }


    @BeforeEach
    void beforeEach() {
        UserRole role = UserRole.valueOf(UserRole.USER.name());
        user = User.builder()
                .username("username")
                .nickname("nickname")
                .password("password")
                .email("email@naver.com")
                .role(role)
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
        when(registryRepository.getReferenceById(anyLong())).thenReturn(registry);
        when(commentRepository.save(any(Comment.class))).thenReturn(save);
        CommentResponseDto comment = commentService.postComment(commentDto);
        verify(commentRepository).save(any(Comment.class));

        // then
        assertEquals(commentDto.getComment(), comment.getComment()); // Comment의 comment가 일치하는지 확인
    }
    
    @Test
    @DisplayName("저장 실패 _ user not found")
    void save_user_not_found() {
        when(userRepository.findByNickname(anyString())).thenReturn(Optional.empty());
        CustomException e = assertThrows(UserNotFoundException.class, () ->
                commentService.postComment(commentDto));
        assertEquals(USER_NOT_FOUND, e.getErrorCode());
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

        CommentResponseDto saveComment = commentService.updateComment(1L,commentDtoEdit, user);
        comment.updateComment(commentDtoEdit.getComment());
        verify(commentRepository).save(comment);

        //then
        assertEquals(saveComment.getCommentId(), comment.getIdx());
        assertEquals(commentDtoEdit.getComment(), comment.getComment()); // Comment 내용이 업데이트 되었는지 확인
    }

    @Test
    @DisplayName("수정 실패 _ registry not found")
    void update_registry_not_found(){
        when(registryRepository.findById(anyLong())).thenReturn(Optional.empty());
        CustomException e = assertThrows(RegistryNotFoundException.class, () ->
                commentService.updateComment(1L, commentDto, user));
        assertEquals(REGISTRY_NOT_FOUND, e.getErrorCode());
    }
    @Test
    @DisplayName("수정 실패 _ comment not found")
    void update_comment_not_found(){
        when(registryRepository.findById(any())).thenReturn(Optional.ofNullable(registry));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        CustomException e = assertThrows(CommentNotFoundException.class, () ->
                commentService.updateComment(1L, commentDto, user));
        assertEquals(COMMENT_NOT_FOUND, e.getErrorCode());
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

    @Test
    @DisplayName("삭제 실패 _ registry not found")
    void delete_registry_not_found(){
        when(registryRepository.findById(anyLong())).thenReturn(Optional.empty());
        CustomException e = assertThrows(RegistryNotFoundException.class, ()->
                commentService.deleteComment(1L, commentDto, user));
        assertEquals(REGISTRY_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("삭제 실패 _ comment not found")
    void delete_comment_not_found(){
        when(registryRepository.findById(anyLong())).thenReturn(Optional.ofNullable(registry));
        when(commentRepository.findById(anyLong())).thenReturn(Optional.empty());
        CustomException e = assertThrows(CommentNotFoundException.class, () ->
                commentService.deleteComment(1L, commentDto, user));
        assertEquals(COMMENT_NOT_FOUND, e.getErrorCode());
    }
}
