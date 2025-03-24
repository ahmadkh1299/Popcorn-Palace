package com.att.tdp.popcorn_palace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for exception handling support methods.
 * This class contains reusable logic for building standardized error responses.
 */
public final class Utils {

    // Private constructor to prevent instantiation (enforces utility class pattern)
    private Utils() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Builds a standardized error response as a ResponseEntity.
     *
     * @param e       The exception to extract message from
     * @param status  The HTTP status to be returned
     * @param request The web request (used to get request path)
     * @return ResponseEntity containing a consistent error body structure
     */
    public static ResponseEntity<Object> buildErrorResponse(Exception e, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());                 // Current time
        body.put("status", status.value());                         // HTTP status code (e.g., 400)
        body.put("error", status.getReasonPhrase());                // Status message (e.g., "Bad Request")
        body.put("message", e.getMessage());                        // Actual exception message
        body.put("path", request.getDescription(false));            // URL or request path

        return new ResponseEntity<>(body, status);
    }
}
