package com.dalcho.adme.batch.tasklets;

import com.dalcho.adme.utils.video.VideoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
public class BatchTasklet implements Tasklet {

    @Value("{spring.servlet.multipart.location}")
    private File locationPath;

    private final VideoUtils videoUtils;

    public BatchTasklet(VideoUtils videoUtils) {
        this.videoUtils = videoUtils;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.debug("executed tasklet.");
        videoUtils.cleanDirectory(locationPath);
        return RepeatStatus.FINISHED;
    }
}
