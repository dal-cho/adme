package com.dalcho.adme.utils.video;

import com.dalcho.adme.exception.invalid.InvalidExtensionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ExtCheckUtils {
    public static String extractionExt(MultipartFile thumbnail) {
        String mimeType = thumbnail.getContentType();
        checkExt(mimeType);
        return mimeType.substring(mimeType.lastIndexOf("/") + 1);
    }

    private static void checkExt(String mimeType) {
        final List<String> PERMISSION_FILE_MIME_TYPE = Arrays.asList("image/gif", "image/jpeg", "image/png");

        if (!PERMISSION_FILE_MIME_TYPE.contains(mimeType)){
            throw new InvalidExtensionException();
        }

        log.info("[checkExt] 확장자 체크 완료");
    }

}
