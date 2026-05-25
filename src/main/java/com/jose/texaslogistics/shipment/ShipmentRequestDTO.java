package com.jose.texaslogistics.shipment;


import jakarta.validation.constraints.NotBlank;

public class ShipmentRequestDTO {

    @NotBlank(message = "Origin is required")
    private String origin;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotBlank(message = "Description is required")
    private String description;

    public String getOrigin(){
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDescription() {
        return description;
    }
}
