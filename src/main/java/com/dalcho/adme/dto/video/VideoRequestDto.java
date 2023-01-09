package com.dalcho.adme.dto.video;

import com.dalcho.adme.domain.VideoFile;
import lombok.*;
import java.time.LocalDateTime;

@Getter
public class VideoRequestDto {
    private String title;
    private String content;

    public VideoRequestDto() {
    }

    public VideoFile toEntity(String uuid, String uploadPath){
        return VideoFile.builder()
                .title(title)
                .content(content)
                .uuid(uuid)
                .uploadPath(uploadPath)
                .videoDate(LocalDateTime.now())
                .build();
    }
}
