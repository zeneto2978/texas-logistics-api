package com.jose.texaslogistics.driver;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class DriverExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationErrors(MethodArgumentNotValidException exception) {

        Map<String, String> erros = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            erros.put(error.getField(), error.getDefaultMessage());
        });

        return Map.of(
                "timestamp", Instant.now(),
                "status", 400,
                "error", "Bad Request",
                "message", "Validation failed",
                "errors", erros
        );
    }

    @ExceptionHandler(DriverNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleDriverNotFound(DriverNotFoundException exception) {
        return Map.of(
                "timestamp", Instant.now(),
                "status", 404,
                "error", "Not Found",
                "message", exception.getMessage()
        );
    }

    @ExceptionHandler(DriverHasAssignmentsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleDriverHasAssignments(DriverHasAssignmentsException exception){
        return Map.of(
                "timestamp", Instant.now(),
                "status", 409,
                "error", "Conflict",
                "message", exception.getMessage()
        );
    }

    @ExceptionHandler(DriverInactiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleDriverInactive(DriverInactiveException exception){
        return Map.of(
                "timestamp", Instant.now(),
                "status", 400,
                "error", "Bad Request",
                "message", exception.getMessage()
        );
    }

    @ExceptionHandler(DriverBusyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleDriverBusy(DriverBusyException exception){
        return Map.of(
                "timestamp", Instant.now(),
                "status", 400,
                "error", "Bad Request",
                "message", exception.getMessage()
        );
    }
}