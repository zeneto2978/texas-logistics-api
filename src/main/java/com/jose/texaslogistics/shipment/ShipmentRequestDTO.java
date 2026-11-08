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

    // setter adicionado para permitir criar o objeto nos testes
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    // setter adicionado para permitir criar o objeto nos testes
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    // setter adicionado para permitir criar o objeto nos testes
    public void setDescription(String description) {
        this.description = description;
    }
}
