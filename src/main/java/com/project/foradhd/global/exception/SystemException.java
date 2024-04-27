package com.project.foradhd.global.exception;

import lombok.Getter;

@Getter
public class SystemException extends RuntimeException {

    public SystemException(Throwable cause) {
        super(cause);
    }
}
