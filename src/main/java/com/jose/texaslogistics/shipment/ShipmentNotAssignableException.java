package com.jose.texaslogistics.shipment;

public class ShipmentNotAssignableException extends RuntimeException {

    public ShipmentNotAssignableException(
            Long shipmentId,
            ShipmentStatus status
    ){
        super(
                "Shipment with id " + shipmentId
                + " cannot be assigned because its status is "
                + status
        );
    }
}
