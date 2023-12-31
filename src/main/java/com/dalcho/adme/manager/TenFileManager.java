package com.dalcho.adme.manager;

import lombok.Getter;

@Getter
public class TenFileManager extends AbstractFileManager {
    private final String tenFolderPath;

    public TenFileManager(String tenFolderPath) {
        super(tenFolderPath);
        this.tenFolderPath = tenFolderPath;
    }
}
