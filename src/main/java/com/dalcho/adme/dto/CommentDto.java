package com.dalcho.adme.dto;

import com.dalcho.adme.domain.Registry;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private String nickname;
    private String comment;
    private Long registryIdx;
}
