package com.dalcho.adme.dto.video;

import com.dalcho.adme.domain.VideoFile;
import lombok.*;
import java.time.LocalDateTime;

@Getter
public class VideoRequestDto {
    private String title;
    private String content;

    public VideoRequestDto() {
    }

    public VideoRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
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

    public VideoFile toEntity(VideoFile videoFile) {
        return VideoFile.builder()
                .title(videoFile.getTitle())
                .content(videoFile.getContent())
                .uploadPath(videoFile.getUploadPath())
                .videoDate(LocalDateTime.now())
                .build();
    }
}