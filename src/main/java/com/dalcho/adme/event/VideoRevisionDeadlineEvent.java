package com.dalcho.adme.event;

import com.dalcho.adme.domain.VideoFile;
import lombok.Getter;

@Getter
public class VideoRevisionDeadlineEvent {

    private final VideoFile videoFile;

    public VideoRevisionDeadlineEvent(VideoFile videoFile) {
        this.videoFile = videoFile;
    }
}
