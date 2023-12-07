package com.dalcho.adme.dto.video;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
@Getter
public class VideoMultipartFile {

    private final MultipartFile videoFile;
    private final UUID uuid;
    private final String videoS3FileName;
    private final int setTime;

    public VideoMultipartFile(MultipartFile videoFile, int setTime) {
        this.videoFile = videoFile;
        this.setTime = setTime;
        int index = getOriginalFilename().lastIndexOf(".");
        String ext = getOriginalFilename().substring(index + 1);
        this.uuid = UUID.randomUUID();
        this.videoS3FileName = uuid + "." + ext;
    }

    public InputStream getInputStream() throws IOException {
        return this.videoFile.getInputStream();
    }
    public String getOriginalFilename() {
        return this.videoFile.getOriginalFilename();
    }
}
