package com.dalcho.adme.exception.invalid;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;

public class InvalidPatternException extends CustomException {
    public InvalidPatternException() {
        super(ErrorCode.INVALID_PATTERN);
    }
}
