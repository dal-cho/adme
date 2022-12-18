package com.dalcho.adme.dto;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String nickname;
    private String comment;
    private Long registryIdx;

    // dto → entity
    public Comment toEntity(Registry registry, User user) {
        return Comment.builder()
                .user(user)
                .comment(comment)
                .registry(registry)
                .build();
    }
}
