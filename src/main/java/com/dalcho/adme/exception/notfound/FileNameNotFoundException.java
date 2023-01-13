package com.dalcho.adme.exception.notfound;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;

public class FileNameNotFoundException extends CustomException {
    public FileNameNotFoundException() {
        super(ErrorCode.FILE_NAME_NOT_FOUND);
    }
}
