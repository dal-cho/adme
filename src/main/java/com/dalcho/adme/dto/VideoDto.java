package com.dalcho.adme.dto;

import lombok.Data;

@Data
public class VideoDto {
    private String fileName;
    private String uuid;
    private String uploadPath;
    private boolean image;
}
