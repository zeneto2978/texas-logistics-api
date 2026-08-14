package com.jose.texaslogistics.shipment;

public class ShipmentNotInTransitException extends RuntimeException{

    public ShipmentNotInTransitException(Long shipmentId, ShipmentStatus status) {

        super(
                "Shipment with id " + shipmentId
                    + " cannot be completed because its status is "
                    + status
        );
    }
}
