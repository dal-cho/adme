package com.dalcho.adme.common;

public enum CommonResponse {

    SUCCESS(0, "Success"),
    FAIL(-1, "Fail");

    final int code;
    final String msg;

    CommonResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
