package com.dalcho.adme.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class VideoDto {
    private String fileName;
    private String uploadPath;
    private Long fileSize;
    private String fileType;
    private byte[] fileData;
    private LocalDateTime videoDate;
}
