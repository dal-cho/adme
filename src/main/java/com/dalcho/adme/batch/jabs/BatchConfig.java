package com.dalcho.adme.batch.jabs;

import com.dalcho.adme.batch.tasklets.BatchTasklet;
import com.dalcho.adme.domain.VideoFile;
import com.dalcho.adme.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory; // job 빌더 생성용
    private final StepBuilderFactory stepBuilderFactory; // Step 빌더 생성용
    private final EntityManagerFactory entityManagerFactory;
    private final VideoService videoService;
    private final BatchTasklet batchTasklet;

    // JobBuilderFactory를 통해서 Job을 생성
    @Bean
    public Job VideoJob() throws Exception {
        return jobBuilderFactory.get("VideoJob")
                .start(Step())
                .next(Step2())
                .build();
    }

    // 10분 지난 원본 동영상 삭제 (Chunk 방식)
    @Bean
    @JobScope
    public Step Step() throws Exception {
        return stepBuilderFactory.get("OriginalFileDeleteStep")
                .<VideoFile, VideoFile> chunk(5)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    // 썸네일 폴더 와 10초 동영상 폴더 비우기 (Tasklet 방식)
    @Bean
    @JobScope
    public Step Step2() throws Exception {
        return stepBuilderFactory.get("ThumbnailTenVideoClearStep")
                .tasklet(batchTasklet)
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<VideoFile> reader() throws Exception {
        JpaPagingItemReader<VideoFile> reader = new JpaPagingItemReader<VideoFile>() {
            @Override
            public int getPage() {
                return 0;
            }
        };

        reader.setQueryString("SELECT vf FROM VideoFile vf WHERE video_date <= CURRENT_TIMESTAMP AND status = 'valid' order by created_at desc");
        reader.setPageSize(5);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setName("JpaPagingItemReader");

        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<VideoFile, VideoFile> processor() {
        return new ItemProcessor<VideoFile, VideoFile>() {
            @Override
            public VideoFile process(VideoFile item) throws Exception {
                log.info("유효 아이디: "+ String.valueOf(item.getId()));
                return videoService.deleteLocalFiles(item.getId());
            }
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<VideoFile> writer() {
        return new JpaItemWriterBuilder<VideoFile>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
