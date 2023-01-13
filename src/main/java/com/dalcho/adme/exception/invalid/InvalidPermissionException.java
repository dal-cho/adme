package com.dalcho.adme.exception.invalid;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;

import static com.dalcho.adme.exception.ErrorCode.PERMISSION_DENIED;

// 해당 권한이 없을 때
public class InvalidPermissionException extends CustomException {
    public InvalidPermissionException(){
        super(ErrorCode.PERMISSION_DENIED);
    }
}
