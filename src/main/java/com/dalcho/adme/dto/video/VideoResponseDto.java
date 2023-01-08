package com.dalcho.adme.dto.video;

import com.dalcho.adme.domain.VideoFile;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VideoResponseDto {
    private String title;
    private String content;
    private String uuid;
    private String uploadPath;
    private LocalDateTime videoDate;

    public VideoResponseDto() {
    }

    public VideoResponseDto(String title, String content, String uuid, String uploadPath, LocalDateTime videoDate) {
        this.title = title;
        this.content = content;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.videoDate = videoDate;
    }

    public static VideoResponseDto of(VideoFile videoFile) {
        return new VideoResponseDto(
                videoFile.getTitle(),
                videoFile.getContent(),
                videoFile.getUuid(),
                videoFile.getUploadPath(),
                videoFile.getVideoDate()
        );
    }
}
