package com.jose.texaslogistics.driver;

public class DriverHasAssignmentsException extends RuntimeException{

    public DriverHasAssignmentsException(Long id) {

        super("Driver with id " + id + " cannot be deleted because it has assignments.");
    }
}
