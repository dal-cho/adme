package com.dalcho.adme.controller;

import com.dalcho.adme.dto.VideoDto;
import com.dalcho.adme.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
@Slf4j
public class VideoController {

    public final VideoService videoService;

    @PostMapping(value = "/10s/videos")
    public String uploadFile(@RequestParam("uploadFile") MultipartFile file, VideoDto dto) throws Exception {
        log.info("VideoController");
        // @ModelAttribute 가 default 값
        videoService.uploadFile(file, dto);
        return "tenseconds";
    }
}
