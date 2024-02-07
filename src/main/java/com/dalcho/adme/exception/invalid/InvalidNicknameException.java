package com.dalcho.adme.exception.invalid;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;

public class InvalidNicknameException extends CustomException {
    public InvalidNicknameException() {
        super(ErrorCode.INVALID_NICKNAME);
    }
}