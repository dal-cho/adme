package com.dalcho.adme.config;

import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@Configuration
public class FfmpegConfig {
    @Value("${ffmpeg.path}")
    private String ffmpegPath;
    @Value("${ffprobe.path}")
    private String ffprobePath;

    @Bean
    public FFmpeg ffmpeg() {
        try {
            return new FFmpeg(ffmpegPath);
        } catch (IOException e) {
            log.error("[FfmpegConfig] ffmpeg bean create error");
        }
        System.exit(1);
        return null;
    }

    @Bean
    public FFprobe ffprobe() {
        try {
            return new FFprobe(ffprobePath);
        } catch (IOException e) {
            log.error("[FfmpegConfig] ffprobe bean create error");
        }
        System.exit(1);
        return null;
    }
}
