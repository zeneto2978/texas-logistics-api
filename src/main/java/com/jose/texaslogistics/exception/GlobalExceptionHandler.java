package com.jose.texaslogistics.exception;


import com.jose.texaslogistics.assignment.AssignmentNotFoundException;
import com.jose.texaslogistics.driver.DriverBusyException;
import com.jose.texaslogistics.driver.DriverHasAssignmentsException;
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

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DriverNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDriverNotFound(
            DriverNotFoundException exception) {
        return buildErrorResponse(
                404,
                "Not Found",
                exception.getMessage()
        );
    }

    @ExceptionHandler(DriverHasAssignmentsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDriverHasAssignments(
            DriverHasAssignmentsException exception) {

        return buildErrorResponse(
                409,
                "Conflict",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ShipmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleShipmentNotFound(
            ShipmentNotFoundException exception) {
        return buildErrorResponse(
                404,
                "Not Found",
                exception.getMessage()
        );
    }

    @ExceptionHandler(AssignmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAssignmentNotFound(
            AssignmentNotFoundException exception) {
        return buildErrorResponse(
                404,
                "Not Found",
                exception.getMessage()
        );
    }

    @ExceptionHandler(DriverInactiveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDriverInactive(
            DriverInactiveException exception) {
        return buildErrorResponse(
                400,
                "Bad Request",
                exception.getMessage()
        );
    }

    @ExceptionHandler(DriverBusyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDriverBusy(
            DriverBusyException exception) {
        return buildErrorResponse(
                400,
                "Bad Request",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ShipmentNotAssignableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleShipmentNotAssignable(
            ShipmentNotAssignableException exception) {
        return buildErrorResponse(
                400,
                "Bad Request",
                exception.getMessage()
        );
    }

    @ExceptionHandler(ShipmentNotInTransitException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleShipmentNotInTransit(
            ShipmentNotInTransitException exception) {
        return buildErrorResponse(
                400,
                "Bad Request",
                exception.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidationsErrors(
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

        return ValidationErrorResponse.of(errors);
    }

    private ErrorResponse buildErrorResponse(
            int status,
            String error,
            String message) {

        return ErrorResponse.of(status, error, message);
    }
}
