package com.dalcho.adme.event;

import com.dalcho.adme.utils.video.VideoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoRevisionDeadlineListener {
    private final VideoUtils videoUtils;
    @Async
    @EventListener
    public void deleteVideoEvent(VideoRevisionDeadlineEvent event) throws InterruptedException {
        Thread.sleep(600 * 1000); // 10분 시간 설정
        log.info("[VideoRevisionDeadlineListener] videoDeleteEvent 동작");
        videoUtils.deleteFile(event.getPath());
    }
}
