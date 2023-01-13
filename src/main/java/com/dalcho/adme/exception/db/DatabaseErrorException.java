package com.dalcho.adme.exception.db;

import com.dalcho.adme.exception.CustomException;
import com.dalcho.adme.exception.ErrorCode;

public class DatabaseErrorException extends CustomException {
    public DatabaseErrorException(){
        super(ErrorCode.DATABASE_ERROR);
    }
}
