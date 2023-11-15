package com.dalcho.adme.batch.tasklets;

import com.dalcho.adme.utils.video.LocalFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BatchTasklet implements Tasklet {
    private final LocalFileUtils localFileUtils;

    public BatchTasklet(LocalFileUtils localFileUtils) {
        this.localFileUtils = localFileUtils;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.debug("executed tasklet.");
        localFileUtils.cleanThumbnailTenVideoDirectory();
        return RepeatStatus.FINISHED;
    }
}
