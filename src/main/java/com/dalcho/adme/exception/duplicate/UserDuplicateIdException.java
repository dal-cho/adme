package com.dalcho.adme.exception.duplicate;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;

public class UserDuplicateIdException extends CustomException {
    public UserDuplicateIdException() {
        super(ErrorCode.USER_DUPLICATE_ID);
    }
}
