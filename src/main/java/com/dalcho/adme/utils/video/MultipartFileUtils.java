package com.dalcho.adme.utils.video;

import com.dalcho.adme.domain.VideoFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    public static void saveFile(MultipartFile file, VideoFile videoFile) {
        log.info("[MultipartFileUtils] saveFile 수행");

        Path path = Paths.get(videoFile.getUploadPath());

        try {
            // 디렉토리가 없으면 작성
            FileDirectoryUtils.createDirectories(path);

            // 입력 스트림을 파일로 내보내기 location+filename 경로로 파일 내보내기
            Files.copy(file.getInputStream(), path.resolve(videoFile.getUuid() + ".mp4"));

        } catch (IOException e) {
            throw new IllegalStateException("failed to save file. " + videoFile.getUuid(), e);
        }
    }

    public static void deleteFile(VideoFile videoFile) {
        log.info("[MultipartFileUtils] deleteFile 수행");

        Path path = Paths.get(videoFile.getUploadPath() + File.separator + videoFile.getUuid() + ".mp4");
        Path thumb = Paths.get(videoFile.getUploadPath() + File.separator + "thumb_" + videoFile.getUuid() + ".jpg");

        try {
            Files.delete(path);
            Files.delete(thumb);
        } catch (IOException e) {
            throw new IllegalStateException("failed to delete file. " + videoFile.getUuid(), e);
        }
    }
}