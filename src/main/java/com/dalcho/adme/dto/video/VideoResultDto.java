package com.dalcho.adme.dto.video;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoResultDto {
    private boolean success;
    private int code;
    private String msg;
    private String title;

    @Builder
    public VideoResultDto(String title) {
        this.title = title;
    }
}
