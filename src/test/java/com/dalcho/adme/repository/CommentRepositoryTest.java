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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DisplayName("H2를 이용한 Comment TEST")
@TestPropertySource(locations = "/application.properties")
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
        Registry registry = Registry.builder()
                .nickname("coco")
                .title("안녕하세요")
                .main("hi")
                .build();
        Registry saveRegistry = registryRepository.save(registry);
        commentDto = new CommentDto("commentNickname","comment", saveRegistry.getIdx());
//        comment = new Comment(commentDto);
//        comment = commentRepository.save(commentDto.toEntity());
    }


    @Test
    @DisplayName("comment 저장")
    void saveComment() {
        System.out.println(commentDto.toString());
        Registry registry = registryRepository.getReferenceById(commentDto.getRegistryIdx());
        Comment saveComment = commentRepository.save(commentDto.toEntity(registry));

        assertThat(registry).isSameAs(saveComment.getRegistry());
        assertThat("comment").isEqualTo(saveComment.getComment());
        //assertThat(commentRepository.count()).isEqualTo(1);

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

        CommentDto comment1 = new CommentDto();
        comment1.setComment("wow");
        comment1.setNickname("hh");

        //when
        Registry saveRegistry = registryRepository.save(registry.toEntity());
//        comment.setRegistryIdx(saveRegistry.getIdx());
//        comment1.setRegistryIdx(saveRegistry.getIdx());
        Comment saveComment = commentRepository.save(comment.toEntity(saveRegistry));
        Comment saveComment1 = commentRepository.save(comment1.toEntity(saveRegistry));

        //then
        Long idx = saveRegistry.getIdx();

        List<Comment> results = commentRepository.findAllByRegistry_Idx(idx);
        assertThat(saveComment.getComment()).isEqualTo(results.get(0).getComment());
        assertThat(saveComment1.getComment()).isEqualTo(results.get(1).getComment());
    }

}
