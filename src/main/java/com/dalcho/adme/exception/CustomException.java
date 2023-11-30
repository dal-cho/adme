package com.dalcho.adme.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 4663380430591151694L;
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
