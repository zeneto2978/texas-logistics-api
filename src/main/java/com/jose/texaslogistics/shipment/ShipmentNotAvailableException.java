package com.jose.texaslogistics.shipment;

public class ShipmentNotAvailableException extends RuntimeException {

    public ShipmentNotAvailableException(
            Long shipmentId,
            ShipmentStatus status
    ) {
        super(
                "Shipment with id " + shipmentId
                        + " cannot be assigned because its status is "
                        + status
        );
    }
}