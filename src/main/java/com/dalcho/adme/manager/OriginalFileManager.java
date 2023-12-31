package com.dalcho.adme.manager;

import lombok.Getter;

@Getter
public class OriginalFileManager extends AbstractFileManager {
    private final String originalFolderPath;

    public OriginalFileManager(String originalFolderPath) {
        super(originalFolderPath);
        this.originalFolderPath = originalFolderPath;
    }
}
