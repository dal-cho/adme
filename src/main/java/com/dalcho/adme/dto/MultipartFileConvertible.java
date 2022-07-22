package com.dalcho.adme.dto;

import lombok.Data;

@Data
public class MultipartFileConvertible {
    private String filename;
    private String originalFilename;
    private String contentType;
    private BZip2Data data;
}
