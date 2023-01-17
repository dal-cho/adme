package com.dalcho.adme.service.Impl;

import com.dalcho.adme.common.CommonResponse;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.video.VideoRequestDto;
import com.dalcho.adme.dto.video.VideoResponseDto;
import com.dalcho.adme.dto.video.VideoResultDto;
import com.dalcho.adme.exception.notfound.FileNotFoundException;
import com.dalcho.adme.exception.notfound.UserNotFoundException;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.repository.VideoRepository;
import com.dalcho.adme.service.VideoService;
import com.dalcho.adme.system.OSValidator;
import com.dalcho.adme.utils.video.FfmpegUtils;
import com.dalcho.adme.utils.video.MultipartFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service("VideoService")
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final OSValidator osValidator;

    @Value("${ffmpeg.path}")
    private String ffmpegPath;
    @Value("${ffprobe.path}")
    private String ffprobePath;

    @Override
    @Transactional
    public VideoResultDto uploadFile(User user, VideoRequestDto videoRequestDto, MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new FileNotFoundException();
        }

        user = userRepository.findByNickname(user.getNickname()).orElseThrow(UserNotFoundException::new);

        String uuid = UUID.randomUUID().toString();
        String uploadPath = osValidator.checkOs();

        VideoFile videoFile = videoRequestDto.toEntity(uuid, uploadPath);
        videoFile.setUser(user);

        log.info("[uploadFile] data 저장 수행");
        // Static 폴더에 파일 저장 -> 추후 Server 에 저장
        MultipartFileUtils.saveFile(file, videoFile);

        log.info("[uploadFile] Thumbnail 생성 및 저장 수행");
        FfmpegUtils.createThumbnail(ffmpegPath, ffprobePath, videoFile);

        videoRepository.save(videoFile);

        VideoResultDto videoResultDto = VideoResultDto.builder()
                .title(videoRequestDto.getTitle())
                .build();

        setSuccessResult(videoResultDto);

        return videoResultDto;
    }

    @Override
    public List<VideoResponseDto> getList(Pageable pageable) {
        Page<VideoFile> videoFiles = videoRepository.findAll(pageable);
        return videoFiles.stream()
                .map(VideoResponseDto::of)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public VideoResultDto update(Long id, VideoRequestDto videoRequestDto, MultipartFile file) throws IOException {

        VideoFile videoFile = videoRepository.findById(id).orElseThrow(FileNotFoundException::new);
        if (!file.isEmpty()) {
            MultipartFileUtils.deleteFile(videoFile);
        }

        String uuid = UUID.randomUUID().toString();
        String uploadPath = osValidator.checkOs();

        videoFile.update(videoRequestDto.toEntity(uuid, uploadPath));

        MultipartFileUtils.saveFile(file, videoFile);

        FfmpegUtils.createThumbnail(ffmpegPath, ffprobePath, videoFile);

        VideoResultDto videoResultDto = VideoResultDto.builder()
                .title(videoRequestDto.getTitle())
                .build();

        setSuccessResult(videoResultDto);

        return videoResultDto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        VideoFile videoFile = videoRepository.findById(id).orElseThrow(FileNotFoundException::new);
        MultipartFileUtils.deleteFile(videoFile);
        videoRepository.deleteById(id);
    }

    private void setSuccessResult(VideoResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
}