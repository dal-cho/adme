package com.dalcho.adme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VideoDto {
    // 비디오 URL(이미지 관리 방식으로 진행중)
    private String video;
    // 비디오 제목
    private String title;
    // 비디오 설명
    private String comment;
    // 비디오에 설정한 태그
    private String tag;

    // 이미지 업로드용
    private String fileName;
    private String uploadPath;
    private String uuid;
    private boolean image;
}
