package com.dalcho.adme.event;

import com.dalcho.adme.utils.video.VideoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VideoRevisionDeadlineListener {
    @Async
    @EventListener
    public void deleteVideoEvent(VideoRevisionDeadlineEvent event) throws InterruptedException {
        Thread.sleep(300 * 1000); // 5분 시간 설정 *내부 시스템에서는 삭제 후에도 1분뒤에 반영된다.
        log.info("[VideoRevisionDeadlineListener] videoDeleteEvent 동작");
        VideoUtils.deleteVideo(event.getVideoFile());
    }
}
