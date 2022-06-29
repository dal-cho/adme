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
public class TenSeconds {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)// id 자동 카운트
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private String uploadPath;

    @Column(nullable = false)
    private boolean image;

    public TenSeconds(VideoDto videoDto) {
        this.fileName = videoDto.getFileName();
        this.uuid = videoDto.getUuid();
        this.uploadPath = videoDto.getUploadPath();
        this.image = videoDto.isImage();
    }
}
