package com.dalcho.adme.domain;

import com.dalcho.adme.dto.VideoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VideoFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)// id 자동 카운트
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String uploadPath;

    @Column(nullable = false)
    private String image;

//    @Column(nullable = false)
//    private String videoDate;

    public VideoFile(VideoDto videoDto) {
        this.fileName = videoDto.getFileName();
        this.uploadPath = String.valueOf(videoDto.getUploadPath());
        this.image = String.valueOf(videoDto.getImage());
//        this.videoDate = String.valueOf(videoDto.getVideoDate());
    }
}
