package com.project.foradhd.global.exception;

public class PostScrapException extends RuntimeException {
    public PostScrapException(String message) {
        super(message);
    }

    public PostScrapException(String message, Throwable cause) {
        super(message, cause);
    }
}
