package com.jose.texaslogistics.exception;

import java.time.Instant;
import java.util.Map;

public record ValidationErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        Map<String, String> errors
) {

    public static ValidationErrorResponse of(Map<String, String> errors) {
        return new ValidationErrorResponse(
                Instant.now(),
                400,
                "Bad Request",
                "Validation failed",
                errors
        );
    }
}
