package com.dalcho.adme.controller;

import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.VideoDto;
import com.dalcho.adme.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
public class VideoController {

    public final VideoService videoService;

    @GetMapping( "/tenseconds/list" )
    public List<VideoFile> listFiles() throws Exception {
        log.info("VideoController GetList");
        return videoService.getList();
    }

    @PostMapping(value = "/tenseconds/videos")
    public VideoFile uploadFile(@RequestParam("uploadFile") MultipartFile file) throws Exception {
        log.info("VideoController Post");
        return videoService.uploadFile(file);
    }
}
