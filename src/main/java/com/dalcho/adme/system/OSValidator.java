package com.dalcho.adme.system;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OSValidator {

    @Value("${spring.servlet.multipart.location}")
    private String macUploadLocation;
    @Value("${window.location}")
    private String windowUploadLocation;

    private static String OS = System.getProperty("os.name").toLowerCase();
    public static boolean IS_WINDOWS = (OS.indexOf("win") >= 0);
    public static boolean IS_MAC = (OS.indexOf("mac") >= 0);

    public String checkOs() {
        if (IS_WINDOWS) {
            return windowUploadLocation;
        } else if (IS_MAC) {
            return macUploadLocation;
        }
        return "Your OS is not support!!";
    }
}
