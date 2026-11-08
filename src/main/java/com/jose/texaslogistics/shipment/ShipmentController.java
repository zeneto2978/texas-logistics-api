package com.jose.texaslogistics.shipment;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shipments")
@Tag(
        name = "Shipments",
        description = "Operations for managing logistic shipments"
)
public class ShipmentController {

    private final ShipmentService shipmentService;

    public ShipmentController(ShipmentService shipmentService){
        this.shipmentService = shipmentService;
    }

    @PostMapping
    @Operation(summary = "Create a new shipment", description = "Registers a new shipment in the logistics system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Shipment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid shipment data")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ShipmentResponseDTO createShipment(@Valid @RequestBody ShipmentRequestDTO requestDTO){

        return shipmentService.createShipment(requestDTO);
    }

    @GetMapping
    @Operation(summary = "List shipments", description = "Returns shipments with pagination, sorting and optional status filtering.")
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
    @Operation(summary = "Find shipment by ID", description = "Returns a shipment registered in the logistic system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shipment found successfully"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    public ShipmentResponseDTO getShipmentById(@PathVariable Long id){
        return shipmentService.getShipmentById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a shipment", description = "Updates the information of an existing shipment.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Shipment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid shipment data"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    public ShipmentResponseDTO updateShipment(
            @PathVariable Long id,
            @Valid
            @RequestBody ShipmentRequestDTO requestDTO) {
        return shipmentService.updateShipment(id,requestDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a shipment", description = "Deletes an existing shipment from the logistics system.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Shipment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Shipment not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShipment(@PathVariable Long id) {
        shipmentService.deleteShipment(id);
    }
}
