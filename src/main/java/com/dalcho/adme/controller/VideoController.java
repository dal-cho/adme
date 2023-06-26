package com.dalcho.adme.controller;

import com.dalcho.adme.domain.User;
import com.dalcho.adme.dto.registry.PagingDto;
import com.dalcho.adme.dto.video.VideoRequestDto;
import com.dalcho.adme.dto.video.VideoResponseDto;
import com.dalcho.adme.dto.video.VideoResultDto;
import com.dalcho.adme.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VideoController {

    public final VideoService videoService;

    @GetMapping("/tenSeconds/list/{curPage}")
    public PagingDto<Object> listFiles(@PathVariable int curPage) throws Exception {
        log.info("[VideoController] GetList");
        return videoService.getList(curPage);
    }

    @GetMapping("/tenSeconds/video/{id}")
    public VideoResponseDto getFile(@PathVariable long id) throws Exception {
        log.info("[VideoController] GetFile");
        return videoService.getFile(id);
    }

    @PostMapping("/tenSeconds/videos")
    public VideoResultDto uploadFile(@AuthenticationPrincipal User user, @RequestPart(name = "sideData") VideoRequestDto requestDto,
                                     @RequestPart(name = "videoFile") MultipartFile file, @RequestPart(name = "thumbnail", required = false) MultipartFile thumbnail) throws Exception {
        log.info("[VideoController] uploadFile()");
        return videoService.uploadFile(user, requestDto, file, thumbnail);
    }

    @PutMapping("/tenSeconds/video/{id}")
    public VideoResultDto update(@PathVariable Long id, @RequestPart(name = "updateData") VideoRequestDto requestDto,
                                 @RequestPart(name = "thumbnail", required = false) MultipartFile thumbnail) throws IOException {
        log.info("[VideoController] update");
        return videoService.update(id, requestDto, thumbnail);
    }

    @DeleteMapping("/tenSeconds/video/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        videoService.delete(id, user);
    }
}

