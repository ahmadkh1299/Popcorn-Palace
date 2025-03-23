package com.att.tdp.popcorn_palace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public final class Utils {

    private Utils() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    public static ResponseEntity<Object> buildErrorResponse(Exception e, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", e.getMessage());
        body.put("path", request.getDescription(false));
        return new ResponseEntity<>(body, status);
    }
}
