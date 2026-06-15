package com.jose.texaslogistics.driver;

public class DriverInactiveException extends RuntimeException{

    public DriverInactiveException(Long driverId){
        super("Driver with id " + driverId + " is inactive");
    }
}
