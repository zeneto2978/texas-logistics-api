package com.jose.texaslogistics.driver;

public class DriverBusyException extends RuntimeException{

    public DriverBusyException(Long driverId){
        super("Driver with id " + driverId + " is busy");
    }
}
