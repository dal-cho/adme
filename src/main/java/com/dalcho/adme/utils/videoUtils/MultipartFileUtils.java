package com.dalcho.adme.utils.videoUtils;

import com.dalcho.adme.dto.VideoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * MultipartFile 관련 유틸리티
 */
@Slf4j
@RequiredArgsConstructor
public class MultipartFileUtils {

    /**
     * 파일을 저장합니다.
     */
    public static void saveFile(MultipartFile file, VideoDto videoDto) {

        Path path = Paths.get(videoDto.getUploadPath());

        log.info("MultipartFileUtils");
        Assert.notNull( file , "file can't be null" );
        try {
            if ( file.isEmpty()) {
                throw new IllegalArgumentException( "cloud not save empty file. " + videoDto.getUuid() );
            }

            // 디렉토리가 없으면 작성
            FileDirectoryUtils.createDirectories(path);

            // 입력 스트림을 파일로 내보내기 location+filename 경로로 파일 내보내기
            Files.copy( file.getInputStream(), path.resolve(videoDto.getUuid()+".mp4") );

        } catch( IOException e ) {
            throw new IllegalStateException( "failed to save file. " + videoDto.getUuid(), e );
        }

    }
}