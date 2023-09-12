package com.dalcho.adme.utils.video;

import com.dalcho.adme.domain.VideoFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@Component
public class VideoUtils {
    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;

    public void saveFile(MultipartFile file, VideoFile videoFile) {
        log.info("[VideoUtils] saveFile");

        Path path = Paths.get(videoFile.getUploadPath());

        try {
            // 디렉토리가 없으면 작성
            FileDirectoryUtils.createDirectories(path);

            // 입력 스트림을 파일로 내보내기 location+filename 경로로 파일 내보내기
            Files.copy(file.getInputStream(), path.resolve(videoFile.getUuid() + ".mp4"));

        } catch (IOException e) {
            throw new IllegalStateException("failed to save file. " + videoFile.getUuid(), e);
        }

        log.info("[VideoUtils] saveFile Completed!");
    }

    public void saveThumbnail(VideoFile videoFile, MultipartFile thumbnail) throws IOException {
        log.info("[VideoUtils] saveThumbnail");

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

        File outputFile = new File(videoFile.getUploadPath() + File.separator + "thumb_" + videoFile.getUuid() + "." + videoFile.getThumbnailExt());
        ImageIO.write(croppedImage, videoFile.getThumbnailExt(), outputFile);

        log.info("[VideoUtils] saveThumbnail Completed!");
    }

    public void createVideo(VideoFile videoFile, int setTime) {
        log.info("[VideoUtils] createVideo");

        FFmpegBuilder fFmpegBuilder = new FFmpegBuilder()
                .setInput(videoFile.getUploadPath() + videoFile.getUuid() + ".mp4") // 변환 할 파일 위치 설정
                .overrideOutputFiles(true) // 덮어쓰기 설정
                .addExtraArgs("-ss", String.valueOf(setTime)) // 영상 자를 위치 설정
                .addExtraArgs("-t", String.valueOf(10)) // 영상 길이 설정
                .addOutput(videoFile.getUploadPath() + "ten_" + videoFile.getUuid() + ".mp4") // 변환 된 파일 위치 설정
                .setFormat("mp4") // 확장자
                .setVideoResolution(1280, 720) // 비디오 해상도 설정
                .setVideoFrameRate(30) // 프레임 설정
                .setVideoBitRate(3000 * 1000) // 비트 레이트 설정 (화질 설정)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe); // ffmpeg: 동영상편집, ffprobe: 음성편집
        executor.createJob(fFmpegBuilder).run();

        log.info("[VideoUtils] createVideo Completed!");
    }

    public void createThumbnail(VideoFile videoFile, int setTime) {
        log.info("[VideoUtils] createThumbnail");

        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true) // 출력이 있는경우 덮어쓰기
                .setInput(videoFile.getUploadPath() + videoFile.getUuid() + ".mp4") // 썸네일 생성대상 파일
                .addExtraArgs("-ss", String.valueOf(setTime)) // 썸네일 추출 시작점
                .addOutput(videoFile.getUploadPath() + "thumb_" + videoFile.getUuid() + "." + videoFile.getThumbnailExt()) // 썸네일 파일을 저장할 위치
                .setFrames(1) // 프레임 수
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

        log.info("[VideoUtils] createThumbnail Completed!");
    }

    public void deleteFile(Path path) {
        log.info("[VideoUtils] deleteFile");
        try {
            Files.delete(path);
            log.info("[VideoUtils] deleteFile Completed!");
        } catch (IOException e) {
            throw new IllegalStateException("failed to delete File.");
        }
    }

    public void deleteFiles(VideoFile videoFile) {
        log.info("[VideoUtils] deleteFiles");

        Path ten = Paths.get(videoFile.getUploadPath() + File.separator + "ten_" + videoFile.getUuid() + ".mp4");
        Path thumb = Paths.get(videoFile.getUploadPath() + File.separator + "thumb_" + videoFile.getUuid() + "." + videoFile.getThumbnailExt());

        try {
            Files.delete(ten);
            Files.delete(thumb);
            log.info("[VideoUtils] deleteFiles Completed!");
        } catch (IOException e) {
            throw new IllegalStateException("failed to delete Files. " + videoFile.getUuid(), e);
        }
    }

    public void cleanDirectory(File file) {
        log.info("[VideoUtils] CleanUpFiles");

        try {
            FileUtils.cleanDirectory(file);
            log.info("[VideoUtils] CleanUpFiles Completed!");
        } catch (IOException e) {
            throw new IllegalStateException("failed to delete File.");
        }
    }

}
