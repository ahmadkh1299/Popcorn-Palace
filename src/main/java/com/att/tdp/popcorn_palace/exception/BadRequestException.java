package com.att.tdp.popcorn_palace.exception;

/**
 * Custom exception for HTTP 400 Bad Request responses.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
