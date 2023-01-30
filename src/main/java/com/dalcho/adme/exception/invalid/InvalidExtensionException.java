package com.dalcho.adme.exception.invalid;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;

public class InvalidExtensionException extends CustomException {
    public InvalidExtensionException() {
        super(ErrorCode.INVALID_EXTENSION);
    }
}
