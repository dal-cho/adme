package com.dalcho.adme.exception.notfound;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;

public class FileNotFoundException extends CustomException {

//    private static final long serialVersionUID= -6212475941372852475L;

    public FileNotFoundException() {
        super(ErrorCode.FILE_NOT_FOUND);
    }

}

