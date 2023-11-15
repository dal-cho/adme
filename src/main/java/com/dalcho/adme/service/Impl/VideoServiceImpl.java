package com.dalcho.adme.service.Impl;

import com.dalcho.adme.common.CommonResponse;
import com.dalcho.adme.domain.User;
import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.PagingDto;
import com.dalcho.adme.dto.video.*;
import com.dalcho.adme.exception.invalid.InvalidPermissionException;
import com.dalcho.adme.exception.notfound.FileNotFoundException;
import com.dalcho.adme.exception.notfound.UserNotFoundException;
import com.dalcho.adme.repository.UserRepository;
import com.dalcho.adme.repository.VideoRepository;
import com.dalcho.adme.service.VideoService;
import com.dalcho.adme.utils.video.FfmpegUtils;
import com.dalcho.adme.utils.video.LocalFileUtils;
import com.dalcho.adme.utils.video.S3Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service("VideoService")
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final S3Utils s3Utils;
    private final LocalFileUtils localFileUtils;
    private final FfmpegUtils ffmpegUtils;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private static final int PAGE_POST_COUNT = 12; // 한 페이지 에 존재 하는 게시글 수

    @Override
    @Transactional
    public VideoResultDto uploadFile(UserDetails userInfo, VideoRequestDto videoRequestDto, MultipartFile originalFile, MultipartFile thumbnail) throws IOException {
        log.info("[VideoService] uploadFile");

        if (originalFile.isEmpty()) {
            throw new FileNotFoundException();
        }

        User user = userRepository.findByNickname(userInfo.getUsername()).orElseThrow(UserNotFoundException::new);
        VideoMultipartFile videoMultipartFile = new VideoMultipartFile(originalFile);

        // 원본 동영상 로컬 저장
        localFileUtils.saveOriginalFile(videoMultipartFile);

        // 10초 비디오 생성 후 S3 업로드
        ffmpegUtils.createTenVideo(videoMultipartFile);
        String s3TenVideoUrl = s3Utils.tenVideoUpload(videoMultipartFile);
        String s3ThumbnailUrl;
        File localThumbnailFile;

        // 썸네일 업로드 유무에 따른 생성 및 저장 후 S3 업로드
        if (thumbnail.isEmpty()) {
            localThumbnailFile = ffmpegUtils.createThumbnail(videoMultipartFile);
        } else {
            localThumbnailFile = localFileUtils.saveThumbnailFile(videoMultipartFile.getUuid(), thumbnail);
        }

        s3ThumbnailUrl = s3Utils.thumbnailUpload(localThumbnailFile);

        // DB 저장
        VideoFile videoFile = videoRequestDto.toEntity(videoMultipartFile.getOriginalFilename(), videoMultipartFile.getVideoS3FileName(), s3ThumbnailUrl, s3TenVideoUrl);
        videoFile.setUser(user);
        videoRepository.save(videoFile);

        VideoResultDto videoResultDto = VideoResultDto.builder()
                .title(videoRequestDto.getTitle())
                .build();

        setSuccessResult(videoResultDto);

        log.info("[VideoService] uploadFile END");
        return videoResultDto;
    }

    @Override
    public PagingDto<VideoFile> getList(int curPage) {
        log.info("[VideoService] getList");
        Pageable pageable = PageRequest.of(curPage - 1, PAGE_POST_COUNT);
        Page<VideoFile> videoFiles = videoRepository.findAll(pageable);
        return PagingDto.of(videoFiles);
    }

    @Override
    public VideoResponseDto getFile(long id) {
        log.info("[VideoService] getFile");
        VideoFile videoFile = videoRepository.findById(id).orElseThrow(FileNotFoundException::new);
        return VideoResponseDto.toEntity(videoFile);
    }

    @Override
    @Transactional
    public VideoResultDto update(Long id, VideoRequestDto videoRequestDto, MultipartFile thumbnail) throws IOException {
        log.info("[VideoService] update START");

        VideoFile videoFile = videoRepository.findById(id).orElseThrow(FileNotFoundException::new);

        videoFile.update(videoRequestDto.toUpdateEntity());

        // 1. 썸네일 변경
        if (!thumbnail.isEmpty()) {
            // 기존 thumbnail 삭제
            localFileUtils.deleteThumbnailFile(videoFile.getThumbnailName());
            s3Utils.deleteThumbnail(videoFile.getThumbnailName());
            log.info("[VideoServiceImpl] 기존 thumbnail 삭제 수행");

            // 새로운 thumbnail 저장
            File localThumbnailFile = localFileUtils.saveThumbnailFile(videoFile.getUuid(), thumbnail);
            String s3ThumbnailUrl = s3Utils.thumbnailUpload(localThumbnailFile);
            videoFile.updateThumbnailUrl(s3ThumbnailUrl);
            log.info("[VideoServiceImpl] 변경된 thumbnail 저장 수행");
        }

        // 2. 수정 시간 10분을 초과 했는지 체크
        if (videoFile.limitTimeCheck()) {
            log.info("[VideoServiceImpl] 10분 수정 시간 초과");

            VideoResultDto videoResultDto = VideoResultDto.builder()
                    .title(videoRequestDto.getTitle())
                    .build();

            setSuccessResult(videoResultDto);
            return videoResultDto;
        }

        // 3. 10초 영상 변경
        boolean originVideoCheck = Objects.nonNull(videoFile.getStatus()); // 원본 파일이 존재 하는지 체크
        boolean setTimeCheck = videoRequestDto.getSetTime() > 0; // setTime 의 변경이 있는지 체크

        // 원본 파일이 존재 하고 setTime 의 변경이 있을 경우 (원본 파일이 있어 야지 setTime 에 맞는 10초 영상을 변경할 수 있다.)
        if (originVideoCheck && setTimeCheck) {
            // 기존 10초 영상 삭제 (파일, S3)
            localFileUtils.deleteTenVideoFile(videoFile.getUuid());
            s3Utils.deleteTenVideo(videoFile.getS3FileName());
            log.info("[VideoServiceImpl] 기존 10초 비디오 삭제 수행");

            // 새로운 10초 영상 생성 (파일, S3)
            ffmpegUtils.createTenVideo(videoFile);
            String s3TenVideoUrl = s3Utils.tenVideoUpload(videoFile);
            videoFile.updateTenVideoUrl(s3TenVideoUrl);
            log.info("[VideoServiceImpl] 변경된 10초 비디오 생성 및 저장 수행");
        }

        VideoResultDto videoResultDto = VideoResultDto.builder()
                .title(videoRequestDto.getTitle())
                .build();

        setSuccessResult(videoResultDto);

        log.info("[VideoService] update END");
        return videoResultDto;
    }

    @Override
    @Transactional
    public void delete(Long id, UserDetails user) {
        log.info("[VideoService] delete");
        VideoFile videoFile = videoRepository.findById(id).orElseThrow(FileNotFoundException::new);

        if(!videoFile.hasAuthentication(user)) {
            log.info("[VideoServiceImpl] hasAuthentication");
            throw new InvalidPermissionException();
        }

        // Local 삭제
        localFileUtils.deleteOriginalFile(videoFile.getS3FileName());
        localFileUtils.deleteThumbnailFile(videoFile.getThumbnailName());
        localFileUtils.deleteTenVideoFile(videoFile.getUuid());

        // S3 삭제
        s3Utils.deleteThumbnail(videoFile.getS3FileName());
        s3Utils.deleteTenVideo(videoFile.getS3FileName());

        // DB 삭제
        videoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public VideoFile deleteLocalFiles(Long id) {
        log.info("[VideoService] deleteLocalFiles");
        VideoFile videoFile = videoRepository.findById(id).orElseThrow(NullPointerException::new);

        localFileUtils.deleteOriginalFile(videoFile.getS3FileName());
        videoFile.setStatus(null);
        return videoRepository.save(videoFile);
    }

    private void setSuccessResult(VideoResultDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

}