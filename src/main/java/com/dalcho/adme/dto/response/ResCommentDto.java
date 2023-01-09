package com.dalcho.adme.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ResCommentDto {
    private String commentName;
    private String registryNickname;
    private Long commentId;
    private String comment;
}
