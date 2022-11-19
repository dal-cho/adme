package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.CommentDto;
import com.dalcho.adme.dto.LoginDto;
import com.dalcho.adme.dto.RegistryDto;
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
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("H2를 이용한 Comment TEST")
@TestPropertySource(locations = "/application.properties")
@ExtendWith( SpringExtension. class )
@SpringBootTest( webEnvironment = SpringBootTest . WebEnvironment . RANDOM_PORT )
@Transactional
public class CommentRepositoryTest {
    @Autowired
    RegistryRepository registryRepository;
    @Autowired
    CommentRepository commentRepository;

    CommentDto commentDto;
    Comment comment;
    User user = new User("testUsername","testNickname","testPassword","PasswordConfirm","testEmail");


    @BeforeEach
    void beforeEach() {
        commentDto = new CommentDto("commentNickname", "testComment", 1L,"registryNickname");
        comment = new Comment(commentDto);
    }


    @Test
    @DisplayName("comment 저장")
    void saveComment() {
        Comment saveComment = commentRepository.save(comment);

        assertThat(comment).isSameAs(saveComment);
        assertThat(comment.getComment()).isEqualTo(saveComment.getComment());
        assertThat(commentRepository.count()).isEqualTo(1);

    }


    @Test
    @DisplayName("id가 1인 댓글")
    void showComment() throws IOException {
        //given
        RegistryDto registry = new RegistryDto();
        registry.setTitle("첫 번째");
        registry.setMain("1");
        registry.setNickname("nickname");


        CommentDto comment = new CommentDto();
        comment.setComment("funfun");
        comment.setNickname("hh");
        comment.setRegistryId(1L);
        comment.setRegistryNickname("nickname");

        CommentDto comment1 = new CommentDto();
        comment1.setComment("wow");
        comment1.setNickname("hh");
        comment1.setRegistryId(1L);
        comment1.setRegistryNickname("nickname");

        //when
        Registry saveRegistry = registryRepository.save(new Registry(registry));
        Comment saveComment = commentRepository.save(new Comment(comment));
        Comment saveComment1 = commentRepository.save(new Comment(comment1));

        //then
        Long idx = saveRegistry.getIdx();
        List<Comment> results = commentRepository.findAllByRegistryId(idx);
        assertThat(saveComment.getComment()).isEqualTo(results.get(0).getComment());
        assertThat(saveComment1.getComment()).isEqualTo(results.get(1).getComment());
    }

}
