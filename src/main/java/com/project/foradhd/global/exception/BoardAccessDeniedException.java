package com.project.foradhd.global.exception;

public class BoardAccessDeniedException extends RuntimeException{
    public BoardAccessDeniedException(String message) {
        super(message);
    }
}
