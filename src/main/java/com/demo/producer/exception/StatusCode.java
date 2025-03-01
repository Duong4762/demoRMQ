package com.demo.producer.exception;

public enum StatusCode {
    COMPLETELY(00),
    FAILED(99);
    private final int code;
    StatusCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

}
