package com.dalcho.adme.utils.video;

import com.dalcho.adme.dto.video.VideoMultipartFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class LocalFileUtils {
    @Value("${original.location}")
    private String originalLocation;
    @Value("${thumbnail.location}")
    String thumbnailDirectory;
    @Value("${ten.location}")
    String tenVideoDirectory;

    public void saveOriginalFile(VideoMultipartFile videoFile) {
        log.info("[LocalFileUtils] SaveOriginalFile");

        Path path = Paths.get(originalLocation);

        try {
            // 입력 스트림을 파일로 내보내기 location+filename 경로로 파일 내보내기
            Files.copy(videoFile.getInputStream(), path.resolve(videoFile.getVideoS3FileName()));

        } catch (IOException e) {
            throw new IllegalStateException("failed to save file. " + videoFile.getVideoS3FileName(), e);
        }

        log.info("[LocalFileUtils] saveFile Completed!");
    }

    public File saveThumbnailFile(UUID uuid, MultipartFile thumbnail) throws IOException {
        log.info("[LocalFileUtils] saveThumbnail");

        // thumbnail 이름 설정
        String originalFilename = thumbnail.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(index + 1);
        String thumbName = uuid + "." + ext; // colum : s3file_name

        // 가로 세로 비율 16:9 설정
        int xRatio = 16;
        int yRatio = 9;

        BufferedImage thumbImage = ImageIO.read(thumbnail.getInputStream());
        int width = thumbImage.getWidth();
        int height = thumbImage.getHeight();

        // 이미지 편집
        BufferedImage croppedImage = thumbImage.getHeight() >= thumbImage.getWidth() ?
                thumbImage.getSubimage(0, (height / 2) - ((width / (xRatio * 2)) * yRatio), width, (width / xRatio) * yRatio) :
                thumbImage.getSubimage((width / 2) - ((height / (yRatio * 2)) * xRatio), 0, (height / yRatio) * xRatio, height);

        File outputFile = new File(thumbnailDirectory + thumbName);
        ImageIO.write(croppedImage, ext, outputFile);

        log.info("[LocalFileUtils] saveThumbnail Completed!");

        return outputFile;
    }

    public void deleteThumbnailFile(String thumbnailName) {
        log.info("[LocalFileUtils] deleteThumbnailFile");

        Path path = Path.of(thumbnailDirectory + thumbnailName);

        try {
            Files.delete(path);
            log.info("[LocalFileUtils] deleteThumbnailFile Completed!");
        } catch (IOException e) {
            throw new IllegalStateException("failed to deleteThumbnailFile.");
        }
    }

    // 로컬 10초 비디오 파일 삭제
    public void deleteTenVideoFile(UUID uuid) {
        log.info("[LocalFileUtils] deleteTenVideoFile");

        Path path = Path.of(tenVideoDirectory + uuid + ".mp4");

        try {
            Files.delete(path);
            log.info("[LocalFileUtils] deleteTenVideoFile Completed!");
        } catch (IOException e) {
            throw new IllegalStateException("failed to deleteTenVideoFile.");
        }
    }

    public void cleanThumbnailTenVideoDirectory() throws IOException {
        log.info("[LocalFileUtils] cleanThumbnailTenVideoDirectory");
        File thumbnailPath = new File(thumbnailDirectory);
        File tenVideoPath = new File(tenVideoDirectory);
        try {
            FileUtils.cleanDirectory(thumbnailPath);
            FileUtils.cleanDirectory(tenVideoPath);

            log.info("[LocalFileUtils] cleanThumbnailTenVideoDirectory Completed!");
        } catch (IOException e) {
            throw new IllegalStateException("failed to cleanThumbnailTenVideoDirectory.");
        }

    }

    public void deleteOriginalFile(String originalFileName) {
        log.info("[LocalFileUtils] deleteOriginalFiles");

        Path path = Path.of(originalLocation + originalFileName);

        try {
            Files.delete(path);
            log.info("[LocalFileUtils] deleteOriginalFiles Completed!");
        } catch (IOException e) {
            throw new IllegalStateException("failed to delete File.");
        }
    }
}
