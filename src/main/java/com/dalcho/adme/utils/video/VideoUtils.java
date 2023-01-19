package com.dalcho.adme.utils.video;

import com.dalcho.adme.domain.VideoFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Slf4j
@RequiredArgsConstructor
public class VideoUtils {

    public static void saveFile(MultipartFile file, VideoFile videoFile) {
        log.info("[VideoUtils] saveFile 수행");

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
        log.info("[VideoUtils] deleteFile 수행");

        Path path = Paths.get(videoFile.getUploadPath() + File.separator + videoFile.getUuid() + ".mp4");
        Path thumb = Paths.get(videoFile.getUploadPath() + File.separator + "thumb_" + videoFile.getUuid() + ".jpg");

        try {
            Files.delete(path);
            Files.delete(thumb);
        } catch (IOException e) {
            throw new IllegalStateException("failed to delete file. " + videoFile.getUuid(), e);
        }
    }

    public static void createThumbnail(String ffmpegPath, String ffprobePath, VideoFile videoFile) throws IOException {

        log.info("[VideoUtils] createThumbnail");

        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
        FFprobe ffprobe = new FFprobe(ffprobePath);

        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true) // 출력이 있는경우 덮어쓰기
                .setInput(videoFile.getUploadPath() + videoFile.getUuid() + ".mp4") // 썸네일 생성대상 파일
                .addExtraArgs("-ss", "00:00:01") // 썸네일 추출 시작점
                .addOutput(videoFile.getUploadPath() + "thumb_" + videoFile.getUuid() + ".jpg") // 썸네일 파일을 저장할 위치
                .setFrames(1) // 프레임 수
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }
}
