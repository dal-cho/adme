package com.dalcho.adme.dto.video;

import com.dalcho.adme.domain.VideoFile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VideoResponseDto {
    private long id;
    private String title;
    private String content;
    private String uuid;
    private String uploadPath;
    private LocalDateTime videoDate;
    private String nickname;

    public VideoResponseDto() {
    }

    public VideoResponseDto(long id, String title, String content, String uuid, String uploadPath, LocalDateTime videoDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.videoDate = videoDate;
    }

    @Builder
    public VideoResponseDto(long id, String title, String content, String uuid, String uploadPath, LocalDateTime videoDate, String nickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.videoDate = videoDate;
        this.nickname = nickname;
    }

    public static VideoResponseDto toEntity(VideoFile videoFile, String nickname) {
        return VideoResponseDto.builder()
                .id(videoFile.getId())
                .title(videoFile.getTitle())
                .content(videoFile.getContent())
                .uuid(videoFile.getUuid())
                .uploadPath(videoFile.getUploadPath())
                .videoDate(videoFile.getVideoDate())
                .nickname(nickname)
                .build();
    }
}
