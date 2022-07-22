package com.dalcho.adme.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VideoDto {
    private String fileName;
    private String uploadPath;
    private MultipartFile image;
//    private LocalDateTime videoDate;
}
