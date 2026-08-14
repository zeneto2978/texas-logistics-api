package com.jose.texaslogistics.assignment;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class AssignmentExceptionHandler {

    @ExceptionHandler(AssignmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleAssignmentNotFound(
            AssignmentNotFoundException exception) {

        return Map.of(
                "timestamp", Instant.now(),
                "status", 404,
                "error", "Not Found",
                "message", exception.getMessage()
        );
    }
}
