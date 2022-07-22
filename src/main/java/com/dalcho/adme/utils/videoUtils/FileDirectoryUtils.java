package com.dalcho.adme.utils.videoUtils;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileDirectoryUtils {

    /**
     * 디렉터리가 없으면 만듭니다.
     */
    public static void createDirectory(Path location) {
        try {
            Files.createDirectory(location);
        } catch (FileAlreadyExistsException ignore) {
            // ignore
        } catch (IOException e) {
            throw new IllegalArgumentException("could not create directory. " + location.toString(), e);
        }
    }

    /**
     * 상위 디렉토리를 포함하여 디렉토리가 없는 경우 작성합니다.
     */
    public static void createDirectories(Path location) {
        try {
            Files.createDirectories(location);
        } catch (FileAlreadyExistsException ignore) {
            // ignore
        } catch (IOException e) {
            throw new IllegalArgumentException("could not create directory. " + location.toString(), e);
        }
    }
}
