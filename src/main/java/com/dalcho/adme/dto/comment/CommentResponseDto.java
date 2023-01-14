package com.dalcho.adme.dto.comment;

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
}
