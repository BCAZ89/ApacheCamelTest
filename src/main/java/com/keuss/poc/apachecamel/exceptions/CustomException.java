package com.keuss.poc.apachecamel.exceptions;

import lombok.Getter;

@Getter
public class CustomException extends Exception {

    private final int code;

    public CustomException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public CustomException(String message, int code) {
        super(message);
        this.code = code;
    }
}
