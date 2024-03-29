package com.dalcho.adme.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoFile extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// id 자동 카운트
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String originalFileName; // 원본동영상이름.avi
    private String s3FileName; // uuid.avi
    private String s3ThumbnailUrl;
    private String s3TenVideoUrl; // s3/uuid.mp4
    private String status = "valid";
    private LocalDateTime videoDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setUser(User user) {
        if (this.user != null) {
            this.user.getVideo().remove(this);
        }
        this.user = user;
        if (!user.getVideo().contains(this)) {
            user.addVideo(this);
        }
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Builder
    public VideoFile(String title, String content, String originalFileName, String s3FileName, String s3ThumbnailUrl, String s3TenVideoUrl, LocalDateTime videoDate) {
        this.title = title;
        this.content = content;
        this.originalFileName = originalFileName;
        this.s3FileName = s3FileName;
        this.s3ThumbnailUrl = s3ThumbnailUrl;
        this.s3TenVideoUrl = s3TenVideoUrl;
        this.videoDate = videoDate;
    }

    public void update(VideoFile updateVideoFile) {
        updateTitle(updateVideoFile.getTitle());
        updateContent(updateVideoFile.getContent());
    }

    public void updateTitle(String title) {
        if (title != null) {
            this.title = title;
        }
    }

    public void updateContent(String content) {
        if (content != null) {
            this.content = content;
        }
    }

    public void updateThumbnailUrl(String s3ThumbnailUrl) {
        if (s3ThumbnailUrl != null) {
            this.s3ThumbnailUrl = s3ThumbnailUrl;
        }
    }

    public void updateTenVideoUrl(String s3TenVideoUrl) {
        if (s3TenVideoUrl != null) {
            this.s3TenVideoUrl = s3TenVideoUrl;
        }
    }

    public boolean hasAuthentication(UserDetails user) {
        return this.user.getNickname().equals(user.getUsername());
    }

    public boolean limitTimeCheck() {
        LocalDateTime checkTime = this.getCreatedAt().plusMinutes(10);
        return LocalDateTime.now().isAfter(checkTime);
    }

    public UUID getUuid() {
        String name = this.s3FileName;
        int index = name.lastIndexOf(".");
        return UUID.fromString(name.substring(0,index));
    }

    public String getThumbnailName() {
        String name = this.s3ThumbnailUrl;
        int index = name.lastIndexOf("/");
        return name.substring(index+1);
    }

}
