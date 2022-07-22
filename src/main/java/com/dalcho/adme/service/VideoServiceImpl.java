package com.dalcho.adme.service;

import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.VideoDto;
import com.dalcho.adme.repository.VideoRepository;
import com.dalcho.adme.utils.videoUtils.MultipartFileUtils;
import com.dalcho.adme.utils.videoUtils.NameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service("VideoService")
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService{

    private final MultipartFileUtils multipartFileUtils;
    private final VideoRepository videoRepository;

    // application.properties 에서 설정한 파일 저장 경로
    @Value("${spring.servlet.multipart.location}")
    private String location;

    @Override
    public VideoFile uploadFile(MultipartFile file, VideoDto videoDto) throws IOException {

        log.info("VideoServiceImpl");

        String filename = file.getOriginalFilename();
        String dbUploadFilename = NameUtils.createStoreFileName(filename);

        if ( file.isEmpty()) {
            throw new IllegalArgumentException( "cloud not save empty file. " + filename );
        }

        // Static 폴더에 파일 저장 -> 추후 Server 에 저장
        multipartFileUtils.saveFile(file, filename, location);

        // 서버에 저장하는 파일의 이름은 uuid 설정으로 중복을 피해준다.
        videoDto.setFileName(dbUploadFilename);
        videoDto.setUploadPath(location);
        videoDto.setImage(file);

        VideoFile videoFile = new VideoFile(videoDto);

        videoRepository.save(videoFile);
        return videoFile;
    }
}