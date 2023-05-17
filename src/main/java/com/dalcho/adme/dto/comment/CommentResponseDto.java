package com.dalcho.adme.dto.comment;

import com.dalcho.adme.domain.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CommentResponseDto {
    private String commentNickname;
    private String registryNickname;
    private Long commentId;
    private String comment;
    private LocalDateTime modifiedAt;

    public static CommentResponseDto of (Comment comment){
        return CommentResponseDto.builder()
                .commentId(comment.getIdx())
                .comment(comment.getComment())
                .commentNickname(comment.getUser().getNickname())
                .registryNickname(comment.getRegistry().getUser().getNickname())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}
