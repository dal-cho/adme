package com.dalcho.adme.exception;

/**
 * 파일 부재 중 오류
 */
public class FileNotFoundException extends  RuntimeException {

    private static final long serialVersionUID= -6212475941372852475L;

    /**
     * 생성자
     */
    public FileNotFoundException(String message) {
        super(message);
    }

    /**
     * 생성자
     */
    public FileNotFoundException(Exception e) {
        super(e);
    }
}

