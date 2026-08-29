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

    // setter adicionado para permitir criar o objeto nos testes
    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    // setter adicionado para permitir criar o objeto nos testes
    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }
}