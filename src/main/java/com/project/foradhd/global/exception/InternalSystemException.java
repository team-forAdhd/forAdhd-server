package com.project.foradhd.global.exception;

import lombok.Getter;

@Getter
public class InternalSystemException extends RuntimeException {

    public InternalSystemException(Throwable cause) {
        super(cause);
    }
}
