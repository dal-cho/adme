package com.dalcho.adme.exception.invalid;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidPatternException extends CustomException {

    public final String message;

    public InvalidPatternException() {
        super(ErrorCode.INVALID_PATTERN);
        this.message = ErrorCode.INVALID_PATTERN.getMessage();
    }

}
