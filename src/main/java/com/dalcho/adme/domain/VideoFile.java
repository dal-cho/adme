package com.dalcho.adme.domain;

import com.dalcho.adme.dto.VideoDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)// id 자동 카운트
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String uploadPath;

    @Column(nullable = false)
    private String ext;

    @Column(nullable = false)
    private Long fileSize; // 파일 사이즈 바이트 수

    @Column(nullable = false)
    private String fileType; // 파일 확장자

    @Column(nullable = false)
    private byte[] fileData; // 실제 파일 데티어

    @Column(nullable = false)
    private String videoDate;

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

    public VideoFile(VideoDto videoDto) {
        this.uuid = videoDto.getUuid();
        this.uploadPath = String.valueOf(videoDto.getUploadPath());
        this.ext = videoDto.getExt();
        this.fileSize = videoDto.getFileSize();
        this.fileType = videoDto.getFileType();
        this.fileData = videoDto.getFileData();
        this.videoDate = String.valueOf(videoDto.getVideoDate());
    }
}
