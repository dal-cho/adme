package com.dalcho.adme.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Registry
    REGISTRY_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."),

    // Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."),

    // Video
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    FILE_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 파일의 이름이 존재하지 않습니다."),

    // User
    PERMISSION_DENIED(HttpStatus.UNAUTHORIZED, "사용자 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 관리자 암호입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 패스워드입니다."),
    INVALID_PATTERN(HttpStatus.UNAUTHORIZED, "잘못된 형식의 패턴입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    USER_DUPLICATE_ID(HttpStatus.CONFLICT, "중복된 Nickname 이 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
