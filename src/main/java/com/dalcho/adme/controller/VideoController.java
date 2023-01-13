package com.dalcho.adme.controller;

import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.video.VideoRequestDto;
import com.dalcho.adme.dto.video.VideoResponseDto;
import com.dalcho.adme.dto.video.VideoResultDto;
import com.dalcho.adme.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VideoController {

    public final VideoService videoService;

    @GetMapping( "/tenSeconds/list" )
    public List<VideoResponseDto> listFiles(@PageableDefault(size = 12, sort = "videoDate", direction = Sort.Direction.ASC) Pageable pageable) throws Exception {
        log.info("VideoController GetList");
        return videoService.getList(pageable);
    }

    @PostMapping("/tenSeconds/videos")
    public VideoResultDto uploadFile(@AuthenticationPrincipal User user, @RequestPart(name = "sideData") VideoRequestDto requestDto,
                                     @RequestPart(name = "videoFile") MultipartFile file) throws Exception {
        log.info("[VideoController] uploadFile() ");
        return videoService.uploadFile(user, requestDto, file);
    }
}

