package com.reliaquest.api.exception;

public class UpstreamServerException extends RuntimeException {
    public UpstreamServerException(String message) {
        super(message);
    }

    public UpstreamServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
