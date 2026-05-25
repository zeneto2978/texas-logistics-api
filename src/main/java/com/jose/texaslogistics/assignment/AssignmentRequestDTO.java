package com.jose.texaslogistics.assignment;


import jakarta.validation.constraints.NotNull;

public class AssignmentRequestDTO {
    @NotNull(message = "Driver ID is required")
    private Long driverId;

    @NotNull(message = "Shipment ID is required")
    private Long shipmentId;

    public Long getDriverId() {
        return driverId;
    }

    public Long getShipmentId() {
        return shipmentId;
    }
}
