package com.reliaquest.api.exception;

public class ClientBadRequestException extends RuntimeException {
    public ClientBadRequestException(String message) {
        super(message);
    }
}