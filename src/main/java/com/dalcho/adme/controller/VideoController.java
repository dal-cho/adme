package com.dalcho.adme.controller;

import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.PagingDto;
import com.dalcho.adme.dto.video.VideoRequestDto;
import com.dalcho.adme.dto.video.VideoResponseDto;
import com.dalcho.adme.dto.video.VideoResultDto;
import com.dalcho.adme.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VideoController {

    public final VideoService videoService;

    @GetMapping("/tenSeconds/list/{curPage}")
    public PagingDto<VideoFile> listFiles(@PathVariable int curPage) throws Exception {
        log.info("[VideoController] listFiles");
        return videoService.getList(curPage);
    }

    @GetMapping("/tenSeconds/video/{id}")
    public VideoResponseDto getFile(@PathVariable long id) throws Exception {
        log.info("[VideoController] getFile");
        return videoService.getFile(id);
    }

    @PostMapping("/tenSeconds/videos")
    public VideoResultDto uploadFile(@AuthenticationPrincipal UserDetails user, @RequestPart(name = "sideData") VideoRequestDto requestDto,
                                     @RequestPart(name = "videoFile") MultipartFile videoFile, @RequestPart(name = "thumbnail", required = false) MultipartFile thumbnail) throws Exception {
        log.info("[VideoController] uploadFile()");
        return videoService.uploadFile(user, requestDto, videoFile, thumbnail);
    }

    @PutMapping("/tenSeconds/video/{id}")
    public VideoResultDto update(@PathVariable Long id, @RequestPart(name = "updateData") VideoRequestDto requestDto,
                                 @RequestPart(name = "thumbnail", required = false) MultipartFile thumbnail) throws IOException {
        log.info("[VideoController] update");
        return videoService.update(id, requestDto, thumbnail);
    }

    @DeleteMapping("/tenSeconds/video/{id}")
    public void delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        log.info("[VideoController] delete");
        videoService.delete(id, user);
    }

    @GetMapping("/health")
    public ResponseEntity<Void> healthCheck() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

