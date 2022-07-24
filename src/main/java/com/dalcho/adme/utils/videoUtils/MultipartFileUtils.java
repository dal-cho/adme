package com.dalcho.adme.utils.videoUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
@Component
@RequiredArgsConstructor
public class MultipartFileUtils {
//
//    /**
//     * MultipartFileConvertible로 값을 다시 채웁니다.
//     */
//    public static void convert ( MultipartFile from, MultipartFileConvertible to ) {
//        to.setFilename( from.getName());
//        to.setOriginalFilename( from.getOriginalFilename());
//        to.setContentType( from.getContentType());
//
//        try {
//            to.setData( BZip2Data.of( from.getBytes ()));
//        } catch ( IOException  e ) {
//            log.error ( "failed to getBytes" , e );
//            throw new IllegalArgumentException( e );
//        }
//    }

    /**
     * 파일을 저장합니다.
     */
    public void saveFile(MultipartFile file, String filename, String location) {

        Path path = Paths.get(location);

        log.info("MultipartFileUtils");
        Assert.notNull( file , "file can't be null" );
        try {
            if ( file.isEmpty()) {
                throw new IllegalArgumentException( "cloud not save empty file. " + filename );
            }

            // 디렉토리가 없으면 작성
            FileDirectoryUtils.createDirectories(path);

            // 입력 스트림을 파일로 내보내기 location+filename 경로로 파일 내보내기
            Files.copy( file.getInputStream(), path.resolve(filename) );

        } catch( IOException e ) {
            throw new IllegalStateException( "failed to save file. " + filename, e );
        }
    }
}