package com.dalcho.adme.service.Impl;

import com.dalcho.adme.common.CommonResponse;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.PagingDto;
import com.dalcho.adme.dto.video.VideoRequestDto;
import com.dalcho.adme.dto.video.VideoResponseDto;
import com.dalcho.adme.dto.video.VideoResultDto;
import com.dalcho.adme.exception.invalid.InvalidPermissionException;
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
import java.util.UUID;

@Slf4j
@Service("VideoService")
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final OSValidator osValidator;
    private final VideoUtils videoUtils;
    private static final int PAGE_POST_COUNT = 12; // 한 페이지에 존재하는 게시글 수

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

        videoUtils.saveFile(file, videoFile);

        if (thumbnail == null) {
            videoUtils.createThumbnail(videoFile, videoRequestDto.getSetTime());
            log.info("[VideoServiceImpl] Thumbnail 생성 수행");
        } else {
            String ext = ExtCheckUtils.extractionExt(thumbnail);
            videoFile.setThumbnailExt(ext);
            videoUtils.saveThumbnail(videoFile, thumbnail);
            log.info("[VideoServiceImpl] thumbnail 저장 수행");
        }

        videoUtils.createVideo(videoFile, videoRequestDto.getSetTime());
        log.info("[VideoServiceImpl] 10초 비디오 생성 및 저장 수행");

        videoRepository.save(videoFile);

        VideoResultDto videoResultDto = VideoResultDto.builder()
                .title(videoRequestDto.getTitle())
                .build();

        setSuccessResult(videoResultDto);

        return videoResultDto;
    }

    @Override
    public PagingDto<VideoFile> getList(int curPage) {
        Pageable pageable = PageRequest.of(curPage - 1, PAGE_POST_COUNT);
        Page<VideoFile> videoFiles = videoRepository.findAll(pageable);
        return PagingDto.of(videoFiles);
    }

    @Override
    public VideoResponseDto getFile(long id) {
        VideoFile videoFile = videoRepository.findById(id).orElseThrow(FileNotFoundException::new);
        return VideoResponseDto.toEntity(videoFile);
    }

    @Override
    @Transactional
    public VideoResultDto update(Long id, VideoRequestDto videoRequestDto, MultipartFile thumbnail) throws IOException {

        VideoFile videoFile = videoRepository.findById(id).orElseThrow(FileNotFoundException::new);

        videoFile.update(videoRequestDto.toUpdateEntity());

        // 1. 썸네일 변경
        if (!thumbnail.isEmpty()) {
            // 기존 thumbnail 삭제
            Path thumb = Paths.get(videoFile.getUploadPath() + File.separator + "thumb_" + videoFile.getUuid() + "." + videoFile.getThumbnailExt());
            videoUtils.deleteFile(thumb);
            log.info("[VideoServiceImpl] 기존 thumbnail 삭제 수행");

            // 새로운 thumbnail 저장
            String ext = ExtCheckUtils.extractionExt(thumbnail);
            videoFile.setThumbnailExt(ext);
            videoUtils.saveThumbnail(videoFile, thumbnail);
            log.info("[VideoServiceImpl] 변경된 thumbnail 저장 수행");
        }

        // 2. 수정시간 10분을 초과 했는지 체크
        if (videoFile.limitTimeCheck()) {
            log.info("[VideoServiceImpl] 10분 수정 시간 초과");

            VideoResultDto videoResultDto = VideoResultDto.builder()
                    .title(videoRequestDto.getTitle())
                    .build();

            setSuccessResult(videoResultDto);
            return videoResultDto;
        }

        // 3. 10초 영상 변경
        Path path = Paths.get(videoFile.getUploadPath() + File.separator + videoFile.getUuid() + ".mp4"); // 원본파일 경로
        boolean originVideoCheck = Files.exists(path); // 원본 파일이 존재하는지 체크
        boolean setTimeCheck = videoRequestDto.getSetTime() > 0; // setTime 의 변경이 있는지 체크

        // 원본파일이 존재하고 setTime 의 변경이 있을경우 (원본파일이 있어야지 setTime 에 맞는 10초 영상을 변경할 수 있다.)
        if (originVideoCheck && setTimeCheck) {
            // 기존 10초 영상 삭제
            Path ten = Paths.get(videoFile.getUploadPath() + File.separator + "ten_" + videoFile.getUuid() + ".mp4");
            videoUtils.deleteFile(ten);
            log.info("[VideoServiceImpl] 기존 10초 비디오 삭제 수행");

            // 새로운 10초 영상 생성
            videoUtils.createVideo(videoFile, videoRequestDto.getSetTime());
            log.info("[VideoServiceImpl] 변경된 10초 비디오 생성 및 저장 수행");
        }

        VideoResultDto videoResultDto = VideoResultDto.builder()
                .title(videoRequestDto.getTitle())
                .build();

        setSuccessResult(videoResultDto);

        return videoResultDto;
    }

    @Override
    @Transactional
    public void delete(Long id, User user) {
        VideoFile videoFile = videoRepository.findById(id).orElseThrow(FileNotFoundException::new);

        if(!videoFile.hasAuthentication(user)) {
            log.info("[VideoServiceImpl] hasAuthentication");
            throw new InvalidPermissionException();
        }

        videoUtils.deleteFiles(videoFile);
        videoRepository.deleteById(id);
    }

    private void setSuccessResult(VideoResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
}