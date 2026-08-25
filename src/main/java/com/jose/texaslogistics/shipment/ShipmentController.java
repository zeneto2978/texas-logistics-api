package com.jose.texaslogistics.shipment;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService){
        this.shipmentService = shipmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShipmentResponseDTO createShipment(@Valid @RequestBody ShipmentRequestDTO requestDTO){

        return shipmentService.createShipment(requestDTO);
    }

    @GetMapping
    public Page<ShipmentResponseDTO> getAllShipments(
            @RequestParam(required = false) ShipmentStatus status,
            Pageable pageable) {
        if (status != null) {
            return shipmentService.getShipmentByStatus(
                    status,
                    pageable
            );
        }

        return shipmentService.getAllShipments(pageable);
    }

    @GetMapping("/{id}")
    public ShipmentResponseDTO getShipmentById(@PathVariable Long id){
        return shipmentService.getShipmentById(id);
    }

    @PutMapping("/{id}")
    public ShipmentResponseDTO updateShipment(
            @PathVariable Long id,
            @Valid
            @RequestBody ShipmentRequestDTO requestDTO) {
        return shipmentService.updateShipment(id,requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
    }
}
