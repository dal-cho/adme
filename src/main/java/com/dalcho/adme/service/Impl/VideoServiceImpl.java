package com.dalcho.adme.service.Impl;

import com.dalcho.adme.common.CommonResponse;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.video.VideoRequestDto;
import com.dalcho.adme.dto.video.VideoResponseDto;
import com.dalcho.adme.dto.video.VideoResultDto;
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
    public VideoResultDto uploadFile(User user, VideoRequestDto videoRequestDto, MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("cloud not save empty file. " + file.getOriginalFilename());
        }

        if (file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("파일 이름을 찾을 수 없습니다.");
        }
        user = userRepository.findByNickname(user.getNickname()).orElseThrow(() -> {
            log.info("[getSignInResult] 아이디가 존재하지 않습니다.");
            throw new RuntimeException(); // 새로 만들어서 해줘야 좋다. (log 는 핸들러에서 처리)
        });

        String uuid = UUID.randomUUID().toString();
        String uploadPath = osValidator.checkOs();

        VideoFile videoFile = videoRequestDto.toEntity(uuid, uploadPath);
        videoFile.setUser(user);

        log.info("[uploadFile] data 저장 수행");
        // Static 폴더에 파일 저장 -> 추후 Server 에 저장
        MultipartFileUtils.saveFile(file, videoFile);
        log.info("[uploadFile] data 저장 수행 완료");

        log.info("[uploadFile] Thumbnail 생성 및 저장 수행");
        FfmpegUtils.createThumbnail(ffmpegPath, ffprobePath, videoFile);
        log.info("[uploadFile] Thumbnail 생성 및 저장 수행 완료");

        videoRepository.save(videoFile);

        VideoResultDto videoResultDto = VideoResultDto.builder()
                .title(videoRequestDto.getTitle())
                .build();

        setSuccessResult(videoResultDto);

        return videoResultDto;
    }

    public List<VideoResponseDto> getList(Pageable pageable) {
        Page<VideoFile> videoFiles = videoRepository.findAll(pageable);
        return videoFiles.stream()
                .map(VideoResponseDto::of)
                .collect(Collectors.toList());
    }

    private void setSuccessResult(VideoResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
}