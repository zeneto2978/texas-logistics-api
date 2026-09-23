package com.jose.texaslogistics.assignment;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Assignments", description = "Operations for managing driver and shipment assignments")
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    @Operation(summary = "Create a new assignment", description = "Assigns an available driver to a pending shipment.")
    @ResponseStatus(HttpStatus.CREATED)
    public AssignmentResponseDTO createAssignment(@Valid
                                                  @RequestBody
                                                  AssignmentRequestDTO requestDTO) {

        return assignmentService.createAssignment(requestDTO);
    }

    @GetMapping
    @Operation(summary = "List assignment", description = "Returns assignments with pagination and sorting.")
    public Page<AssignmentResponseDTO> getAllAssignments(Pageable pageable) {
        return assignmentService.getAllAssignments(pageable);
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Complete an assignment", description = "Completes an assignment, marks the shipment as DELIVERED and makes the driver AVAILABLE again.")
    public AssignmentResponseDTO completeAssignment(@PathVariable Long id) {
        return assignmentService.completeAssignment(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find assignment by ID", description = "Returns an assignment registered in the logistics system.")
    public AssignmentResponseDTO getAssignmentById(
            @PathVariable Long id) {
        return assignmentService.getAssignmentById(id);
    }

    @GetMapping("/driver/{driverId}")
    @Operation(summary = "List assignments by driver", description = "Returns all assignments associated with a specific driver.")
    public List<AssignmentResponseDTO> getAssignmentByDriver(
            @PathVariable Long driverId) {
        return assignmentService.getAssignmentByDriver(driverId);
    }
}
