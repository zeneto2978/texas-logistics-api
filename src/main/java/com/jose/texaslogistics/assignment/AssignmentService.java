package com.jose.texaslogistics.assignment;


import com.jose.texaslogistics.driver.*;
import com.jose.texaslogistics.shipment.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.jose.texaslogistics.driver.DriverStatus;
import com.jose.texaslogistics.driver.DriverInactiveException;
import com.jose.texaslogistics.driver.DriverBusyException;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public AssignmentResponseDTO createAssignment(AssignmentRequestDTO requestDTO) {
        Driver driver = driverRepository.findById(requestDTO.getDriverId())
                .orElseThrow(() ->
                        new DriverNotFoundException(requestDTO.getDriverId()));

        if (driver.getStatus() == DriverStatus.INACTIVE) {
            throw new DriverInactiveException(driver.getId());
        }

        if (driver.getStatus() == DriverStatus.BUSY) {
            throw new DriverBusyException(driver.getId());
        }

        Shipment shipment = shipmentRepository.findById(requestDTO.getShipmentId())
                .orElseThrow(() ->
                        new ShipmentNotFoundException(requestDTO.getShipmentId()));

        if (shipment.getStatus() != ShipmentStatus.PENDING) {
            throw new ShipmentNotAssignableException(
                    shipment.getId(),
                    shipment.getStatus()
            );
        }

        Assignment assignment = new Assignment();

        assignment.setDriver(driver);
        driver.setStatus(DriverStatus.BUSY);

        assignment.setShipment(shipment);
        shipment.setStatus(ShipmentStatus.IN_TRANSIT);

        Assignment savedAssignment = assignmentRepository.save(assignment);

        return new AssignmentResponseDTO(savedAssignment);
    }

    public AssignmentResponseDTO getAssignmentById(Long id) {

        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(()->
                        new AssignmentNotFoundException(id));

        return new AssignmentResponseDTO(assignment);
    }

    @Transactional
    public AssignmentResponseDTO completeAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(()->
                        new AssignmentNotFoundException(id));

        Driver driver = assignment.getDriver();

        Shipment shipment = assignment.getShipment();

        if (shipment.getStatus() != ShipmentStatus.IN_TRANSIT) {
            throw new ShipmentNotInTransitException(
                    shipment.getId(),
                    shipment.getStatus()
            );
        }

        shipment.setStatus(ShipmentStatus.DELIVERED);

        driver.setStatus(DriverStatus.AVAILABLE);

        return new AssignmentResponseDTO(assignment);
    }


    public Page<AssignmentResponseDTO> getAllAssignments(Pageable pageable) {

        return assignmentRepository.findAll(pageable)
                .map(AssignmentResponseDTO::new);
    }


    public List<AssignmentResponseDTO> getAssignmentByDriver(Long driverId) {

        if (!driverRepository.existsById(driverId)) {
            throw new DriverNotFoundException(driverId);
        }

        return assignmentRepository.findByDriverId(driverId)
                .stream()
                .map(AssignmentResponseDTO::new)
                .toList();
    }
}
