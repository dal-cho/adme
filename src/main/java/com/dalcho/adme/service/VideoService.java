package com.dalcho.adme.service;

import com.dalcho.adme.domain.VideoFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    VideoFile uploadFile(MultipartFile file) throws Exception;
    List<VideoFile> getList() throws Exception;
}
