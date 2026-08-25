package com.jose.texaslogistics.exception;


import com.jose.texaslogistics.assignment.AssignmentNotFoundException;
import com.jose.texaslogistics.driver.DriverBusyException;
import com.jose.texaslogistics.driver.DriverInactiveException;
import com.jose.texaslogistics.driver.DriverNotFoundException;
import com.jose.texaslogistics.shipment.ShipmentNotAssignableException;
import com.jose.texaslogistics.shipment.ShipmentNotFoundException;
import com.jose.texaslogistics.shipment.ShipmentNotInTransitException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DriverNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleDriverNotFound(
            DriverNotFoundException exception) {
        return buildErrorResponse(
                404,
                "Not Found",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ShipmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleShipmentNotFound(
            ShipmentNotFoundException exception) {
        return buildErrorResponse(
                404,
                "Not Found",
                exception.getMessage()
        );
    }

    @ExceptionHandler(AssignmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleAssignmentNotFound(
            AssignmentNotFoundException exception) {
        return buildErrorResponse(
                404,
                "Not Found",
                exception.getMessage()
        );
    }

    @ExceptionHandler(DriverInactiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleDriverInactive(
            DriverInactiveException exception) {
        return buildErrorResponse(
                400,
                "Bad Request",
                exception.getMessage()
        );
    }

    @ExceptionHandler(DriverBusyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleDriverBusy(
            DriverBusyException exception) {
        return buildErrorResponse(
                400,
                "Bad Request",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ShipmentNotAssignableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleShipmentNotAssignable(
            ShipmentNotAssignableException exception) {
        return buildErrorResponse(
                400,
                "Bad Request",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ShipmentNotInTransitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleShipmentNotInTransit(
            ShipmentNotInTransitException exception) {
        return buildErrorResponse(
                400,
                "Bad Request",
                exception.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidationsErrors(
            MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {
                    errors.put(
                            error.getField(),
                            error.getDefaultMessage()
                    );
                });

        return Map.of(
                "timestamp", Instant.now(),
                "status", 400,
                "error", "Bad Request",
                "message", "Validation failed",
                "errors", errors
        );
    }

    private Map<String, Object> buildErrorResponse(
            int status,
            String error,
            String message) {

        return Map.of(
                "timestamp", Instant.now(),
                "status", status,
                "error", error,
                "message", message
        );
    }
}
