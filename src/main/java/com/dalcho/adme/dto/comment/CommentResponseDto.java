package com.dalcho.adme.dto.comment;

import com.dalcho.adme.domain.Comment;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentResponseDto {
    private String commentName;
    private String registryNickname;
    private Long commentId;
    private String comment;

    public static CommentResponseDto of (Comment comment){
        return CommentResponseDto.builder()
                .commentId(comment.getIdx())
                .comment(comment.getComment())
                .commentName(comment.getUser().getNickname())
                .registryNickname(comment.getRegistry().getUser().getNickname())
                .build();
    }
}
