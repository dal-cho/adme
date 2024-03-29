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

    @Builder
    public VideoRequestDto(String title, String content, int setTime) {
        this.title = title;
        this.content = content;
        this.setTime = setTime;
    }

    public VideoFile toEntity(String originalFileName, String s3FileName, String s3ThumbnailUrl, String s3TenVideoUrl) {
        return VideoFile.builder()
                .title(title)
                .content(content)
                .originalFileName(originalFileName)
                .s3FileName(s3FileName)
                .s3ThumbnailUrl(s3ThumbnailUrl)
                .s3TenVideoUrl(s3TenVideoUrl)
                .videoDate(LocalDateTime.now().plusMinutes(10))
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