package com.dalcho.adme.dto;

import com.dalcho.adme.domain.Comment;
import com.dalcho.adme.domain.Registry;
import com.dalcho.adme.repository.RegistryRepository;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String nickname;
    private String comment;
    private Long registryIdx;

    // dto → entity
    public Comment toEntity(Registry registry) {
        return Comment.builder()
                .nickname(nickname)
                .comment(comment)
                .registry(registry)
                .build();
    }
}
