package com.project.foradhd.global.exception;

import lombok.Getter;

@Getter
public class BoardAccessDeniedException extends RuntimeException{
    public BoardAccessDeniedException(Throwable message) {
        super(message);
    }
}
