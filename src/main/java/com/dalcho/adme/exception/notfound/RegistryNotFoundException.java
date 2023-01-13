package com.dalcho.adme.exception.notfound;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;

// 게시글이 없을 때
public class RegistryNotFoundException extends CustomException {
    public RegistryNotFoundException(){
        super(ErrorCode.REGISTRY_NOT_FOUND);
    }
}

