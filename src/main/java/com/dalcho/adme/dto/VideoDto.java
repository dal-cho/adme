package com.dalcho.adme.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class VideoDto {
    private String uuid;
    //    private String thumbnailUrl;
//    private String videoUrl;
    private String uploadPath;
    private String ext;
    private Long fileSize;
    private String fileType;
    private byte[] fileData;
    private LocalDateTime videoDate;
}
