package com.dalcho.adme.utils.video;

import com.dalcho.adme.domain.VideoFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
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
public class VideoUtils {

    public static void saveFile(MultipartFile file, VideoFile videoFile) {
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

    public static void saveThumbnail(VideoFile videoFile, MultipartFile thumbnail) throws IOException {
        log.info("[VideoUtils] saveThumbnail");

        BufferedImage thumbImage = ImageIO.read(thumbnail.getInputStream());
        int width = thumbImage.getWidth();
        int height = thumbImage.getHeight();
        // 이미지 16:9 로 편집
        BufferedImage croppedImage = (thumbImage.getHeight() > thumbImage.getWidth() ?
                thumbImage.getSubimage((width/2) - (height / 18 * 16), 0, height / 9 * 16, height) :
                thumbImage.getSubimage(0, (height/2) - (width / 32 * 9), width, width / 16 * 9));

        File outputFile = new File(videoFile.getUploadPath() + File.separator + "thumb_" + videoFile.getUuid() + ".jpg");
        ImageIO.write(croppedImage, "jpg", outputFile);

        log.info("[VideoUtils] saveThumbnail Completed!");
    }

    public static void deleteFile(VideoFile videoFile) {
        log.info("[VideoUtils] deleteFile");

        Path ten = Paths.get(videoFile.getUploadPath() + File.separator + "ten_" + videoFile.getUuid() + ".mp4");
        Path thumb = Paths.get(videoFile.getUploadPath() + File.separator + "thumb_" + videoFile.getUuid() + ".jpg");

        try {
            Files.delete(ten);
            Files.delete(thumb);
        } catch (IOException e) {
            throw new IllegalStateException("failed to delete file. " + videoFile.getUuid(), e);
        }
        log.info("[VideoUtils] deleteFile Completed!");
    }

    public static void createVideo(String ffmpegPath, String ffprobePath, VideoFile videoFile , int setTime) throws IOException {
        log.info("[VideoUtils] createVideo");

        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
        FFprobe ffprobe = new FFprobe(ffprobePath);

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

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(fFmpegBuilder).run();

        log.info("[VideoUtils] createVideo Completed!");
    }

    public static void createThumbnail(String ffmpegPath, String ffprobePath, VideoFile videoFile, int setTime) throws IOException {
        log.info("[VideoUtils] createThumbnail");

        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
        FFprobe ffprobe = new FFprobe(ffprobePath);

        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true) // 출력이 있는경우 덮어쓰기
                .setInput(videoFile.getUploadPath() + videoFile.getUuid() + ".mp4") // 썸네일 생성대상 파일
                .addExtraArgs("-ss", String.valueOf(setTime)) // 썸네일 추출 시작점
                .addOutput(videoFile.getUploadPath() + "thumb_" + videoFile.getUuid() + ".jpg") // 썸네일 파일을 저장할 위치
                .setFrames(1) // 프레임 수
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

        log.info("[VideoUtils] createThumbnail Completed!");
    }

    public static void deleteVideo(VideoFile videoFile) {
        log.info("[VideoUtils] deleteVideo");

        Path path = Paths.get(videoFile.getUploadPath() + File.separator + videoFile.getUuid() + ".mp4");

        try {
            Files.delete(path);
            log.info("[VideoUtils] deleteVideo Completed!");
        } catch (IOException e) {
            throw new IllegalStateException("failed to delete file. " + videoFile.getUuid(), e);
        }
    }
}
