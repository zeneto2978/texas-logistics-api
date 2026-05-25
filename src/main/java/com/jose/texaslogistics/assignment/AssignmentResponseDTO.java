package com.jose.texaslogistics.assignment;


import java.time.LocalDateTime;

public class AssignmentResponseDTO {

    private Long id;
    private Long driverId;
    private String driverName;
    private Long shipmentId;
    private String shipmentOrigin;
    private String shipmentDestination;
    private LocalDateTime assignmentAt;

    public AssignmentResponseDTO(Assignment assignment) {
        this.id = assignment.getId();
        this.driverId = assignment.getDriver().getId();
        this.driverName = assignment.getDriver().getName();
        this.shipmentId = assignment.getShipment().getId();
        this.shipmentOrigin = assignment.getShipment().getOrigin();
        this.shipmentDestination = assignment.getShipment().getDestination();
        this.assignmentAt = assignment.getAssignedAt();
    }


    public Long getId() {
        return id;
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getDriverName() {
        return driverName;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public String getShipmentOrigin() {
        return shipmentOrigin;
    }

    public String getShipmentDestination() {
        return shipmentDestination;
    }

    public LocalDateTime getAssignmentAt() {
        return assignmentAt;
    }
}
