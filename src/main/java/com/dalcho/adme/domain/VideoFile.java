package com.dalcho.adme.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// id 자동 카운트
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String uploadPath;

    @Column(nullable = false)
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

    @Builder
    public VideoFile(String title, String content, String uuid, String uploadPath, LocalDateTime videoDate) {
        this.title = title;
        this.content = content;
        this.uuid = uuid;
        this.uploadPath = uploadPath;
        this.videoDate = videoDate;
    }

    public void update(VideoFile updateVideoFile) {
        updateTitle(updateVideoFile.getTitle());
        updateContent(updateVideoFile.getContent());
        updateUploadPath(updateVideoFile.getUploadPath());
        updateVideoDate(updateVideoFile.getVideoDate());
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

    public void updateUploadPath(String uploadPath) {
        if (uploadPath != null) {
            this.uploadPath = uploadPath;
        }
    }

    public void updateVideoDate(LocalDateTime videoDate) {
        if (videoDate != null) {
            this.videoDate = videoDate;
        }
    }


}
