package com.dalcho.adme.event;

import lombok.Getter;

import java.nio.file.Path;

@Getter
public class VideoRevisionDeadlineEvent {

    private final Path path;

    public VideoRevisionDeadlineEvent(Path path) {
        this.path = path;
    }
}
