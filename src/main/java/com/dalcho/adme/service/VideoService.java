package com.dalcho.adme.service;

import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.PagingDto;
import com.dalcho.adme.dto.video.VideoRequestDto;
import com.dalcho.adme.dto.video.VideoResponseDto;
import com.dalcho.adme.dto.video.VideoResultDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface VideoService {
    VideoResultDto uploadFile(UserDetails user, VideoRequestDto videoRequestDto, MultipartFile file, MultipartFile thumbnail) throws Exception;

    PagingDto<VideoFile> getList(int curPage) throws Exception;

    VideoResponseDto getFile(long id) throws Exception;

    VideoResultDto update(Long id, VideoRequestDto videoRequestDto, MultipartFile thumbnail) throws IOException;

    void delete(Long id, UserDetails user);

    VideoFile deleteLocalFiles(Long id) throws IOException;
}
