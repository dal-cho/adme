package com.dalcho.adme.utils.video;

import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.dto.video.VideoMultipartFile;
import com.dalcho.adme.manager.OriginalFileManager;
import com.dalcho.adme.manager.TenFileManager;
import com.dalcho.adme.manager.ThumbnailFileManager;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class FfmpegUtils {
    private final FFmpeg ffmpeg;
    private final FFprobe ffprobe;

    private final String originalLocation;
    private final String thumbnailDirectory;
    private final String tenVideoDirectory;

    public FfmpegUtils(FFmpeg ffmpeg, FFprobe ffprobe, OriginalFileManager originalFileManager, ThumbnailFileManager thumbnailFileManager, TenFileManager tenFileManager) {
        this.ffmpeg = ffmpeg;
        this.ffprobe = ffprobe;
        this.originalLocation = originalFileManager.getOriginalFolderPath();
        this.thumbnailDirectory = thumbnailFileManager.getThumbnailFolderPath();
        this.tenVideoDirectory = tenFileManager.getTenFolderPath();
    }

    public File createThumbnail(VideoMultipartFile videoFile) {
        log.info("[FfmpegUtils] createThumbnail");

        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true) // 출력이 있는경우 덮어쓰기
                .setInput(originalLocation + videoFile.getVideoS3FileName()) // 썸네일 생성대상 파일
                .addExtraArgs("-ss", String.valueOf(videoFile.getSetTime())) // 썸네일 추출 시작점
                .addOutput(thumbnailDirectory + videoFile.getUuid() + ".jpg") // 썸네일 파일을 저장할 위치
                .setFrames(1) // 프레임 수
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();

        log.info("[FfmpegUtils] createThumbnail Completed!");

        return new File(thumbnailDirectory + videoFile.getUuid() + ".jpg");
    }

    public void createTenVideo(VideoMultipartFile videoFile) {
        createTenVideo(videoFile.getVideoS3FileName(), videoFile.getUuid() + ".mp4", videoFile.getSetTime());
    }

    public void createTenVideo(VideoFile videoFile, int setTime) {
        createTenVideo(videoFile.getS3FileName(), videoFile.getUuid() + ".mp4", setTime);
    }

    private void createTenVideo(String inputFileName, String OutputFileName, int setTime) {
        log.info("[FfmpegUtils] createTenVideo");

        FFmpegBuilder fFmpegBuilder = new FFmpegBuilder()
                .setInput(originalLocation + inputFileName) // 변환 할 파일 위치 설정
                .overrideOutputFiles(true) // 덮어쓰기 설정
                .addExtraArgs("-ss", String.valueOf(setTime)) // 영상 자를 위치 설정
                .addExtraArgs("-t", String.valueOf(10)) // 영상 길이 설정
                .addOutput(tenVideoDirectory + OutputFileName) // 변환 된 파일 위치 설정
                .setFormat("mp4") // 확장자
                .setVideoResolution(1280, 720) // 비디오 해상도 설정
                .setVideoFrameRate(30) // 프레임 설정
                .setVideoBitRate(3000 * 1000) // 비트 레이트 설정 (화질 설정)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe); // ffmpeg: 동영상편집, ffprobe: 음성편집
        executor.createJob(fFmpegBuilder).run();

        log.info("[FfmpegUtils] createTenVideo Completed!");
    }
}
