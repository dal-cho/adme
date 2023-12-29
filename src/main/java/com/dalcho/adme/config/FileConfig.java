package com.dalcho.adme.config;

import com.dalcho.adme.manager.OriginalFileManager;
import com.dalcho.adme.manager.TenFileManager;
import com.dalcho.adme.manager.ThumbnailFileManager;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class FileConfig {
    @Bean
    public OriginalFileManager originalFileManager(ResourceLoader resourceLoader) throws IOException {
        String ORIGINAL_FOLDER_PATH = File.separator + "original" + File.separator;
        File root = resourceLoader.getResource("file:").getFile();
        String originalFolderPath = root.getAbsolutePath() + ORIGINAL_FOLDER_PATH;
        return new OriginalFileManager(originalFolderPath);
    }

    @Bean
    public ThumbnailFileManager thumbnailFileManager(ResourceLoader resourceLoader) throws IOException {
        String THUMBNAIL_FOLDER_PATH = File.separator + "thumbnail" + File.separator;
        File root = resourceLoader.getResource("file:").getFile();
        String thumbnailFolderPath = root.getAbsolutePath() + THUMBNAIL_FOLDER_PATH;
        return new ThumbnailFileManager(thumbnailFolderPath);
    }

    @Bean
    public TenFileManager tenFileManager(ResourceLoader resourceLoader) throws IOException {
        String TEN_FOLDER_PATH = File.separator + "ten" + File.separator;
        File root = resourceLoader.getResource("file:").getFile();
        String tenFolderPath = root.getAbsolutePath() + TEN_FOLDER_PATH;
        return new TenFileManager(tenFolderPath);
    }
}
