package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.comment.CommentRequestDto;
import com.dalcho.adme.dto.registry.RegistryRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("H2를 이용한 Comment TEST")
@DataJpaTest
public class CommentRepositoryTest {
    @Autowired
    RegistryRepository registryRepository;
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    CommentRequestDto commentDto;
    User saveUser;


    @BeforeEach
    void beforeEach() {
        User user = User.builder()
                .nickname("nickname")
                .password("password")
                .build();
        saveUser = userRepository.save(user);

        Registry registry = Registry.builder()
                .user(saveUser)
                .title("안녕하세요")
                .main("hi")
                .build();
        Registry saveRegistry = registryRepository.save(registry);

        commentDto = new CommentRequestDto("commentNickname", "comment", saveRegistry.getIdx());
    }


    @Test
    @DisplayName("comment 저장")
    void saveComment() {
        Registry registry = registryRepository.getReferenceById(commentDto.getRegistryIdx());
        Comment saveComment = commentRepository.save(commentDto.toEntity(registry, saveUser));

        assertThat(registry).isSameAs(saveComment.getRegistry());
        assertThat("comment").isEqualTo(saveComment.getComment());
    }


    @Test
    @DisplayName("id가 1인 댓글")
    void showComment() throws IOException {
        //given
        RegistryRequestDto registry = new RegistryRequestDto();
        registry.setTitle("첫 번째");
        registry.setMain("1");

        CommentRequestDto comment = new CommentRequestDto();
        comment.setComment("funfun");
        comment.setNickname("hh");

        CommentRequestDto comment1 = new CommentRequestDto();
        comment1.setComment("wow");
        comment1.setNickname("hh");

        //when
        Registry saveRegistry = registryRepository.save(registry.toEntity(saveUser));
        Comment saveComment = commentRepository.save(comment.toEntity(saveRegistry, saveUser));
        Comment saveComment1 = commentRepository.save(comment1.toEntity(saveRegistry, saveUser));

        //then
        Long idx = saveRegistry.getIdx();

        List<Comment> results = commentRepository.findAllByRegistry_Idx(idx);
        assertThat(saveComment.getComment()).isEqualTo(results.get(0).getComment());
        assertThat(saveComment1.getComment()).isEqualTo(results.get(1).getComment());
    }

}
