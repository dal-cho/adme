package com.dalcho.adme.manager;

import lombok.Getter;

@Getter
public class ChatFileManager extends AbstractFileManager {
    private final String folderPath;

    public ChatFileManager(String folderPath){
        super(folderPath);
        this.folderPath = folderPath;
    }
}
