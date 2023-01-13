package com.dalcho.adme.exception.invalid;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;

public class InvalidPasswordException extends CustomException {
    public InvalidPasswordException() {
        super(ErrorCode.INVALID_PASSWORD);
    }
}
