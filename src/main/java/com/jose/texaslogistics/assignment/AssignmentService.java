package com.jose.texaslogistics.assignment;


import com.jose.texaslogistics.driver.Driver;
import com.jose.texaslogistics.driver.DriverNotFoundException;
import com.jose.texaslogistics.driver.DriverRepository;
import com.jose.texaslogistics.shipment.Shipment;
import com.jose.texaslogistics.shipment.ShipmentNotFoundException;
import com.jose.texaslogistics.shipment.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final DriverRepository driverRepository;
    private final ShipmentRepository shipmentRepository;

    public AssignmentService(
            AssignmentRepository assignmentRepository,
            DriverRepository driverRepository,
            ShipmentRepository shipmentRepository) {
        this.assignmentRepository = assignmentRepository;
        this.driverRepository = driverRepository;
        this.shipmentRepository = shipmentRepository;
    }

    public AssignmentResponseDTO createAssignment(AssignmentRequestDTO requestDTO) {
        Driver driver = driverRepository.findById(requestDTO.getDriverId())
                .orElseThrow(() -> new DriverNotFoundException(requestDTO.getDriverId()));

        Shipment shipment = shipmentRepository.findById(requestDTO.getShipmentId())
                .orElseThrow(() -> new ShipmentNotFoundException(requestDTO.getShipmentId()));

        Assignment assignment = new Assignment();

        assignment.setDriver(driver);
        assignment.setShipment(shipment);

        Assignment savedAssignment = assignmentRepository.save(assignment);

        return new AssignmentResponseDTO(savedAssignment);
    }

    public List<AssignmentResponseDTO> getAllAssignment() {
        return assignmentRepository.findAll()
                .stream()
                .map(AssignmentResponseDTO::new)
                .toList();
    }
}
