package com.jose.texaslogistics.shipment;


import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public ShipmentService(ShipmentRepository shipmentRepository){
        this.shipmentRepository = shipmentRepository;
    }

    public ShipmentResponseDTO createShipment(ShipmentRequestDTO requestDTO){
        Shipment shipment = new Shipment();

        shipment.setOrigin(requestDTO.getOrigin());
        shipment.setDestination(requestDTO.getDestination());
        shipment.setDescription(requestDTO.getDescription());

        Shipment savedShipment = shipmentRepository.save(shipment);

        return new ShipmentResponseDTO(savedShipment);
    }

    public List<ShipmentResponseDTO> getAllShipments(){
        return shipmentRepository.findAll()
                .stream()
                .map(ShipmentResponseDTO::new)
                .toList();
    }

    public ShipmentResponseDTO getShipmentById(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(()-> new ShipmentNotFoundException(id));

        return new ShipmentResponseDTO(shipment);
    }

    public ShipmentResponseDTO updateShipment(Long id, ShipmentRequestDTO requestDTO){
        Shipment existingShipment = shipmentRepository.findById(id)
                .orElseThrow(()-> new ShipmentNotFoundException(id));

        existingShipment.setOrigin(requestDTO.getOrigin());
        existingShipment.setDestination(requestDTO.getDestination());
        existingShipment.setDescription(requestDTO.getDescription());

        Shipment updatedShipment = shipmentRepository.save(existingShipment);

        return new ShipmentResponseDTO(updatedShipment);
    }

    public void deleteShipment(Long id){
        if (!shipmentRepository.existsById(id)){
            throw new ShipmentNotFoundException(id);
        }

        shipmentRepository.deleteById(id);
    }
}
