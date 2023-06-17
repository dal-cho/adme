package com.dalcho.adme.service.Impl;

import com.dalcho.adme.common.CommonResponse;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.registry.PagingDto;
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
import com.dalcho.adme.utils.video.ExtCheckUtils;
import com.dalcho.adme.utils.video.VideoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private static final int PAGE_POST_COUNT = 12; // 한 페이지에 존재하는 게시글 수

    @Value("${ffmpeg.path}")
    private String ffmpegPath;
    @Value("${ffprobe.path}")
    private String ffprobePath;

    @Override
    @Transactional
    public VideoResultDto uploadFile(User user, VideoRequestDto videoRequestDto, MultipartFile file, MultipartFile thumbnail) throws IOException {

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

        if (thumbnail == null) {
            log.info("[uploadFile] Thumbnail 생성 및 저장 수행");
            VideoUtils.createThumbnail(ffmpegPath, ffprobePath, videoFile, videoRequestDto.getSetTime());
        } else {
            String ext = ExtCheckUtils.extractionExt(thumbnail);
            videoFile.setThumbnailExt(ext);
            log.info("[uploadFile] thumbnail 저장 수행");
            VideoUtils.saveThumbnail(videoFile, thumbnail);
        }


        log.info("[uploadFile] 10초 비디오 생성 및 저장 수행");
        VideoUtils.createVideo(ffmpegPath, ffprobePath, videoFile, videoRequestDto.getSetTime());

        Path path = Paths.get(videoFile.getUploadPath() + File.separator + videoFile.getUuid() + ".mp4");

        publisher.publishEvent(new VideoRevisionDeadlineEvent(path));

        videoRepository.save(videoFile);

        VideoResultDto videoResultDto = VideoResultDto.builder()
                .title(videoRequestDto.getTitle())
                .build();

        setSuccessResult(videoResultDto);

        return videoResultDto;
    }

    @Override
    public PagingDto<Object> getList(int curPage) {
        Pageable pageable = PageRequest.of(curPage - 1, PAGE_POST_COUNT);
        Page<VideoFile> videoFiles = videoRepository.findAll(pageable);
        List<VideoFile> videoFileList = videoFiles.stream()
                .map(video -> new VideoFile(video.getId(), video.getTitle(), video.getContent(),
                        video.getUuid(), video.getUploadPath(), video.getVideoDate()))
                .collect(Collectors.toList());
        return PagingDto.of(videoFiles, null, videoFileList, videoFiles.getTotalPages());
    }

    @Override
    public VideoResponseDto getFile(long id) {
        VideoFile videoFile = videoRepository.findById(id).orElseThrow(FileNotFoundException::new);
        String nickname = videoFile.getUser().getNickname();
        return VideoResponseDto.toEntity(videoFile, nickname);
    }

    @Override
    @Transactional
    public VideoResultDto update(Long id, VideoRequestDto videoRequestDto, MultipartFile thumbnail) throws IOException {

        VideoFile videoFile = videoRepository.findById(id).orElseThrow(FileNotFoundException::new);

        videoFile.update(videoRequestDto.toUpdateEntity());

        Path path = Paths.get(videoFile.getUploadPath() + File.separator + videoFile.getUuid() + ".mp4");

        boolean thumbnailCheck = thumbnail.isEmpty();
        boolean originVideoCheck = Files.exists(path);
        boolean setTimeCheck = videoRequestDto.getSetTime() > 0;

        if (setTimeCheck && originVideoCheck) {
            Path ten = Paths.get(videoFile.getUploadPath() + File.separator + "ten_" + videoFile.getUuid() + ".mp4");
            VideoUtils.deleteFile(ten);

            log.info("[update] 10초 비디오 생성 및 저장 수행");
            VideoUtils.createVideo(ffmpegPath, ffprobePath, videoFile, videoRequestDto.getSetTime());
        }

        if (!thumbnailCheck) {
            Path thumb = Paths.get(videoFile.getUploadPath() + File.separator + "thumb_" + videoFile.getUuid() + "." + videoFile.getThumbnailExt());
            VideoUtils.deleteFile(thumb);

            String ext = ExtCheckUtils.extractionExt(thumbnail);
            videoFile.setThumbnailExt(ext);
            log.info("[uploadFile] thumbnail 저장 수행");
            VideoUtils.saveThumbnail(videoFile, thumbnail);
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
        VideoUtils.deleteFiles(videoFile);
        videoRepository.deleteById(id);
    }

    private void setSuccessResult(VideoResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
}