package com.dalcho.adme.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentDto {
    private String nickname;
    private String comment;
    private int registryId;
    private String registryNickname;

}
