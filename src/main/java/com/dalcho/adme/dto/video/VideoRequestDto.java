package com.dalcho.adme.dto.video;

import com.dalcho.adme.domain.VideoFile;
import lombok.*;
import java.time.LocalDateTime;

@Getter
public class VideoRequestDto {
    private String title;
    private String content;
    private int setTime;

    public VideoRequestDto() {
    }

    public VideoRequestDto(String title, String content, int setTime) {
        this.title = title;
        this.content = content;
        this.setTime = setTime;
    }

    public VideoFile toEntity(String uuid, String uploadPath) {
        return VideoFile.builder()
                .title(title)
                .content(content)
                .uuid(uuid)
                .uploadPath(uploadPath)
                .videoDate(LocalDateTime.now())
                .build();
    }

    public VideoFile toUpdateEntity() {
        return VideoFile.builder()
                .title(title)
                .content(content)
                .videoDate(LocalDateTime.now())
                .build();
    }
}