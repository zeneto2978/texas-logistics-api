package com.jose.texaslogistics.assignment;


import jakarta.validation.Valid;
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
    public List<AssignmentResponseDTO> getAllAssignments() {
        return assignmentService.getAllAssignment();
    }
}
