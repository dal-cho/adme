package com.dalcho.adme.service.Impl;

import com.dalcho.adme.common.CommonResponse;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.video.VideoRequestDto;
import com.dalcho.adme.dto.video.VideoResponseDto;
import com.dalcho.adme.dto.video.VideoResultDto;
import com.dalcho.adme.event.VideoRevisionDeadlineEvent;
import com.dalcho.adme.exception.notfound.FileNotFoundException;
import com.dalcho.adme.exception.notfound.UserNotFoundException;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.repository.VideoRepository;
import com.dalcho.adme.service.VideoService;
import com.dalcho.adme.system.OSValidator;
import com.dalcho.adme.utils.video.VideoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private final ApplicationEventPublisher publisher;

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
        VideoUtils.saveFile(file, videoFile);

        log.info("[uploadFile] 10초 비디오 생성 및 저장 수행");
        VideoUtils.createVideo(ffmpegPath, ffprobePath, videoFile, videoRequestDto.getSetTime());

        log.info("[uploadFile] Thumbnail 생성 및 저장 수행");
        VideoUtils.createThumbnail(ffmpegPath, ffprobePath, videoFile);

        publisher.publishEvent(new VideoRevisionDeadlineEvent(videoFile));

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
    public VideoResultDto update(Long id, VideoRequestDto videoRequestDto) throws IOException {

        VideoFile videoFile = videoRepository.findById(id).orElseThrow(FileNotFoundException::new);

        videoFile.update(videoRequestDto.toUpdateEntity());

        Path path = Paths.get(videoFile.getUploadPath() + File.separator + videoFile.getUuid() + ".mp4");

        if (Files.exists(path)) {
            log.info("[update] 10초 비디오 생성 및 저장 수행");
            VideoUtils.createVideo(ffmpegPath, ffprobePath, videoFile, videoRequestDto.getSetTime());

            log.info("[update] Thumbnail 생성 및 저장 수행");
            VideoUtils.createThumbnail(ffmpegPath, ffprobePath, videoFile);
        }

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
        VideoUtils.deleteFile(videoFile);
        videoRepository.deleteById(id);
    }

    private void setSuccessResult(VideoResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
}