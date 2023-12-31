package com.dalcho.adme.manager;

import java.io.File;

public class AbstractFileManager {
    public final String path;

    public AbstractFileManager(String path) {
        mkdir(path);
        this.path = path;
    }

    private static void mkdir(String absolutePath) {
        File folder = new File(absolutePath);
        if (!folder.exists()) folder.mkdirs();
    }
}
