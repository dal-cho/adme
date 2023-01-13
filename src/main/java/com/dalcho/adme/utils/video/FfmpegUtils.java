package com.dalcho.adme.utils.video;

import com.dalcho.adme.domain.VideoFile;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;

import java.io.IOException;

@Slf4j
public class FfmpegUtils {

    public static void createThumbnail(String ffmpegPath, String ffprobePath, VideoFile videoFile) throws IOException {

        log.info("FfmpegUtils");

        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
        FFprobe ffprobe = new FFprobe(ffprobePath);

        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true) // 출력이 있는경우 덮어쓰기
                .setInput(videoFile.getUploadPath() + videoFile.getUuid() + ".mp4") // 썸네일 생성대상 파일
                .addExtraArgs("-ss", "00:00:03") // 썸네일 추출 시작점
                .addOutput(videoFile.getUploadPath() + "thumb_" + videoFile.getUuid() + ".jpg") // 썸네일 파일을 저장할 위치
                .setFrames(1) // 프레임 수
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }

}
