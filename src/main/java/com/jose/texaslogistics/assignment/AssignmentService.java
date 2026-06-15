package com.jose.texaslogistics.assignment;


import com.jose.texaslogistics.driver.*;
import com.jose.texaslogistics.shipment.Shipment;
import com.jose.texaslogistics.shipment.ShipmentNotFoundException;
import com.jose.texaslogistics.shipment.ShipmentRepository;
import org.springframework.stereotype.Service;
import com.jose.texaslogistics.driver.DriverStatus;
import com.jose.texaslogistics.driver.DriverInactiveException;
import com.jose.texaslogistics.driver.DriverBusyException;

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

        if (driver.getStatus() == DriverStatus.INACTIVE) {
            throw new DriverInactiveException(driver.getId());
        }

        if (driver.getStatus() == DriverStatus.BUSY) {
            throw new DriverBusyException(driver.getId());
        }

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
