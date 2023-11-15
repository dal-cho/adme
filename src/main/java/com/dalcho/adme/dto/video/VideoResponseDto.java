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
    private String s3ThumbnailUrl;
    private String s3TenVideoUrl;
    private LocalDateTime videoDate;
    private String nickname;

    public VideoResponseDto() {
    }

    @Builder
    public VideoResponseDto(long id, String title, String content, String s3ThumbnailUrl, String s3TenVideoUrl, LocalDateTime videoDate, String nickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.s3ThumbnailUrl = s3ThumbnailUrl;
        this.s3TenVideoUrl = s3TenVideoUrl;
        this.videoDate = videoDate;
        this.nickname = nickname;
    }

    public static VideoResponseDto toEntity(VideoFile videoFile) {
        return VideoResponseDto.builder()
                .id(videoFile.getId())
                .title(videoFile.getTitle())
                .content(videoFile.getContent())
                .s3ThumbnailUrl(videoFile.getS3ThumbnailUrl())
                .s3TenVideoUrl(videoFile.getS3TenVideoUrl())
                .videoDate(videoFile.getVideoDate())
                .nickname(videoFile.getUser().getNickname())
                .build();
    }
}
