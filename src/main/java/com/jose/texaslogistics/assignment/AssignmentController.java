package com.jose.texaslogistics.assignment;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AssignmentResponseDTO createAssignment(@Valid
                                                  @RequestBody
                                                  AssignmentRequestDTO requestDTO) {

        return assignmentService.createAssignment(requestDTO);
    }

    @GetMapping
    public Page<AssignmentResponseDTO> getAllAssignments(Pageable pageable) {
        return assignmentService.getAllAssignments(pageable);
    }

    @PutMapping("/{id}/complete")
    public AssignmentResponseDTO completeAssignment(@PathVariable Long id) {
        return assignmentService.completeAssignment(id);
    }

    @GetMapping("/{id}")
    public AssignmentResponseDTO getAssignmentById(
            @PathVariable Long id) {
        return assignmentService.getAssignmentById(id);
    }

    @GetMapping("/driver/{driverId}")
    public List<AssignmentResponseDTO> getAssignmentByDriver(
            @PathVariable Long driverId) {
        return assignmentService.getAssignmentByDriver(driverId);
    }
}
