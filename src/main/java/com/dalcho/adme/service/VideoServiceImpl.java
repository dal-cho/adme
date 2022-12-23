package com.dalcho.adme.service;

import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.VideoDto;
import com.dalcho.adme.repository.VideoRepository;
import com.dalcho.adme.system.OSValidator;
import com.dalcho.adme.utils.videoUtils.FfmpegUtils;
import com.dalcho.adme.utils.videoUtils.MultipartFileUtils;
import com.dalcho.adme.utils.videoUtils.NameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service("VideoService")
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final OSValidator osValidator;

//    // application.properties 에서 설정한 파일 저장 경로
//    private final String fileUploadLocation = osValidator.checkOs();

    @Value("${ffmpeg.path}")
    private String ffmpegPath;
    @Value("${ffprobe.path}")
    private String ffprobePath;

    @Override
    public VideoFile uploadFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("cloud not save empty file. " + file.getOriginalFilename());
        }

        LocalDateTime now = LocalDateTime.now();
        String filename = file.getOriginalFilename();
        String uploadPath = osValidator.checkOs();

        VideoDto videoDto = VideoDto.builder()
                .uuid(NameUtils.createStoreFileName(filename))
                .uploadPath(uploadPath)
                .ext(NameUtils.extractExt(filename))
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .fileData(file.getBytes())
                .videoDate(now)
                .build();

        // Static 폴더에 파일 저장 -> 추후 Server 에 저장
        MultipartFileUtils.saveFile(file, videoDto);

        //썸네일 만들기
        FfmpegUtils.createThumbnail(ffmpegPath, ffprobePath, videoDto);

        VideoFile videoFile = new VideoFile(videoDto);

//        contents.setVideo(videoRepository.save(videoFile)) // 비디오 db에 저장 + 비디오 Entity 값 리턴
//        contentsRepository.save(contents)

        return videoRepository.save(videoFile);
    }

    public List<VideoFile> getList() {
        return videoRepository.findAll();
    }
}