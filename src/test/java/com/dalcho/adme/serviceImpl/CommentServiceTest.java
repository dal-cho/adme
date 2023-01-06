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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

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

        //given
        Registry registry = Registry.builder()
                .title("안녕하세요")
                .main("hi")
                .user(user)
                .build();

        CommentDto commentDto = new CommentDto();
        commentDto.setComment("funfun");
        commentDto.setNickname("hh");
        commentDto.setRegistryIdx(registry.getIdx());

        Registry referenceById = registryRepository.getReferenceById(any());
        Comment comment = commentDto.toEntity(referenceById, user);

        //when
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
        this.commentDto = new CommentDto();
        this.commentDto.setComment("comment");
        this.commentDto.setNickname((user.getNickname()));
        this.commentDto.setRegistryIdx(registry.getIdx());
    }


    @Test
    @DisplayName("comment 저장")
    void saveComment() throws IOException {
        // given
        Comment save = commentDto.toEntity(registry, user);

        // when
        when(commentRepository.save(any(Comment.class))).thenReturn(save);
        Comment comment = commentService.postComment(commentDto);
        verify(commentRepository).save(any(Comment.class));

        // then
        assertEquals("Comment의 comment가 일치하는지 확인", commentDto.getComment(), comment.getComment());
        //        Comment commentTest = commentRepository.findById(comment.getIdx()).orElseThrow(
//                () -> new NullPointerException("comment 생성 x")
        //       );
    }


    @Test
    @DisplayName("comment 수정")
    void updateComment() throws IOException {
        //given
        saveComment();

        // when
        Comment comment = commentService.updateComment(1L,commentDto, user);

        CommentDto commentDtoEdit = new CommentDto();
        commentDto.setComment("comment-edit");

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        Comment commentTest = commentService.updateComment(anyLong(), commentDtoEdit, user);
        verify(commentRepository).save(any(Comment.class));

        //then
        assertEquals("Comment Id 값이 일치하는지 확인.", comment.getIdx(), commentTest.getIdx());
        assertEquals("Comment 내용이 업데이트 되었는지 확인", commentDtoEdit.getComment(), commentTest.getComment());
    }


    @Test
    @DisplayName("comment 삭제 성공")
    void deleteComment() throws IOException {
        // given
        saveComment();
        List<Comment> allByRegistry_idx = commentRepository.findAllByRegistry_Idx(anyLong());
        when(commentRepository.findAllByRegistry_Idx(anyLong())).thenReturn(allByRegistry_idx);
        verify(commentRepository).findAllByRegistry_Idx(anyLong());

        //when
        commentService.deleteComment(anyLong(), commentDto, user);

        // then
        Optional<Comment> commentTest = commentRepository.findById(anyLong());
        if (commentTest.isPresent())
            throw new IllegalArgumentException("Comment 가 정상적으로 삭제되지 않았습니다.");
        else
            assertEquals("Comment 가 비어있다.", Optional.empty(), commentTest);
    }
}
