package com.jose.texaslogistics.shipment;


import java.time.LocalDateTime;

public class ShipmentResponseDTO {

    private Long id;
    private String origin;
    private String destination;
    private String description;
    private ShipmentStatus status;
    private LocalDateTime createdAt;

    public ShipmentResponseDTO(Shipment shipment){
        this.id = shipment.getId();
        this.origin = shipment.getOrigin();
        this.destination = shipment.getDestination();
        this.description = shipment.getDescription();
        this.status = shipment.getStatus();
        this.createdAt = shipment.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDescription() {
        return description;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
