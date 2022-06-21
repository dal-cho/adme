package com.dalcho.adme.service;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.dto.CommentDto;
import com.dalcho.adme.dto.RegistryDto;
import com.dalcho.adme.repository.CommentRepository;
import com.dalcho.adme.repository.RegistryRepository;
import org.assertj.core.api.Assertions;
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

@DisplayName("H2를 이용한 Comment TEST")
@TestPropertySource(locations = "/application.properties")
@ExtendWith( SpringExtension. class )
@SpringBootTest( webEnvironment = SpringBootTest . WebEnvironment . RANDOM_PORT )
@Transactional
public class CommentServiceTest {
    @Autowired
    CommentService commentService;
    @Autowired RegistryService registryService;
    @Autowired RegistryRepository registryRepository;
    @Autowired CommentRepository commentRepository;


    @Test
    @DisplayName("db 저장")
    void saveComment() throws IOException {
        //given
        CommentDto comment = new CommentDto();
        comment.setComment("funfun");
        comment.setNickname("hh");
        comment.setRegistryId(1);

        //when
        Comment saveComment = commentService.setComment(comment);

        //then
        Assertions.assertThat(comment.getComment()).isEqualTo(saveComment.getComment());
        Assertions.assertThat(comment.getNickname()).isEqualTo(saveComment.getNickname());
        Assertions.assertThat(comment.getRegistryId()).isEqualTo(saveComment.getRegistryId());
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
        comment.setRegistryId(1);

        CommentDto comment1 = new CommentDto();
        comment1.setComment("wow");
        comment1.setNickname("hh");
        comment1.setRegistryId(1);

        //when
        Registry saveRegistry = registryService.setUpload(registry);
        Comment saveComment = commentService.setComment(comment);
        Comment saveComment1 = commentService.setComment(comment1);

        //then
        int idx = Math.toIntExact(saveRegistry.getIdx()); // long을 int로 형변환
        List<Comment> results = commentRepository.findAllByRegistryId(idx);
        Assertions.assertThat(saveComment.getComment()).isEqualTo(results.get(0).getComment());
        Assertions.assertThat(saveComment1.getComment()).isEqualTo(results.get(1).getComment());
    }
}
