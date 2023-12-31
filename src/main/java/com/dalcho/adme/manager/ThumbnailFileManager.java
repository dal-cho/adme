package com.dalcho.adme.manager;

import lombok.Getter;

@Getter
public class ThumbnailFileManager extends AbstractFileManager {
    private final String thumbnailFolderPath;

    public ThumbnailFileManager(String thumbnailFolderPath) {
        super(thumbnailFolderPath);
        this.thumbnailFolderPath = thumbnailFolderPath;
    }
}