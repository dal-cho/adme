package com.dalcho.adme.repository;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("H2를 이용한 TEST")
@DataJpaTest
public class CommentandRegistryTest {
    @Autowired
    RegistryRepository registryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    User userSave;

    @Test
    void commentSave_Identity() {
        User user = User.builder()
                .nickname("coco")
                .password("password")
                .build();
        userSave = userRepository.save(user);

        Registry registry = Registry.builder()
                .title("안녕하세요")
                .main("hi")
                .user(userSave)
                .build();
        registryRepository.save(registry);

        Comment comment = Comment.builder()
                .user(userSave)
                .comment("❤️🧡💛💚💙💜🤎🖤")
                .registry(registry)
                .build();

        commentRepository.save(comment);

        Comment savedComment = commentRepository.findById(1L).get();
        Registry savedRegistry = savedComment.getRegistry();

        Assertions.assertThat("coco").isEqualTo(savedRegistry.getUser().getNickname());
        Assertions.assertThat("❤️🧡💛💚💙💜🤎🖤").isEqualTo(savedComment.getComment());
    }


//    @Test
//    void commentAutoTest() {
//        Registry registry1 = new Registry();
//        registry1.setNickname("coco");
//        registry1.setTitle("안녕하세요");
//        registry1.setMain("hi");
//
//        Registry registry2 = new Registry();
//        registry2.setNickname("coco");
//        registry2.setTitle("안녕하세요");
//        registry2.setMain("hi");
//
//
//        Comment comment1 = new Comment();
//        comment1.setComment("❤️🧡💛💚💙💜🤎🖤");
//        comment1.setNickname("우헤헤");
//        comment1.setRegistryId(5L);
//        comment1.setRegistryNickname("pop");
//        comment1.setRegistry(registry1);
//
//        Comment comment2 = new Comment();
//        comment2.setComment("❤️🧡💛💚💙💜🤎🖤");
//        comment2.setNickname("우헤헤");
//        comment2.setRegistryId(5L);
//        comment2.setRegistryNickname("pop");
//
//        comment2.setRegistry(registry2);
//
//        Registry savedRegistry1 = registryRepository.save(registry1);
//        Comment savedComment1 = commentRepository.save(comment1);
//        Comment savedComment2 = commentRepository.save(comment2);
//        Registry savedRegistry2 = registryRepository.save(registry2);
//
//        System.out.printf("%s idx : %d\n", "comment", savedComment1.getIdx());
//        System.out.printf("%s idx : %d\n", "comment", savedComment2.getIdx());
//        System.out.printf("%s idx : %d\n", "registry", savedRegistry1.getIdx());
//        System.out.printf("%s idx : %d\n", "registry", savedRegistry2.getIdx());
//    }
}
