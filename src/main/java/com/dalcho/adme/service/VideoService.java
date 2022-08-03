package com.dalcho.adme.service;

import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.VideoDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {
    VideoFile uploadFile(MultipartFile file, VideoDto dto) throws Exception;
    List<VideoFile> getList() throws Exception;
}
