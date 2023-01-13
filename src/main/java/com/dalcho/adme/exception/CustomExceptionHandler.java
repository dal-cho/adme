package com.dalcho.adme.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    //private final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class); // Logger를 등록

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e) {
        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }
}
