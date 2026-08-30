package com.jose.texaslogistics.driver;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/drivers")
@Tag(name = "Drivers", description = "Operations for managing logistics drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService){
        this.driverService = driverService;
    }

    @Operation(summary = "Create a new driver",
                description = "Registers a new driver in th logistics system")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverResponseDTO createDriver(@Valid @RequestBody DriverRequestDTO requestDTO){
        return driverService.createDriver(requestDTO);
    }

    @Operation(summary = "List drivers",
                description = "Returns drivers with pagination, sorting and optional status filtering.")
    @GetMapping
    public Page<DriverResponseDTO> getAllDrivers(
            @RequestParam(required = false) DriverStatus status,
            Pageable pageable) {
        if (status != null) {
            return driverService.getDriversByStatus(status, pageable);
        }

        return driverService.getAllDrivers(pageable);
    }

    @Operation(summary = "Find briver By ID",
                description = "Returns a driver registered in th logistics system.")
    @GetMapping("/{id}")
    public DriverResponseDTO getDriverById(@PathVariable Long id){
        return driverService.getDriverById(id);
    }

    @Operation(summary = "Update a driver",
                description = "Updates the information of an existing driver.")
    @PutMapping("/{id}")
    public DriverResponseDTO updateDriver(@PathVariable Long id,
                                          @Valid
                                          @RequestBody DriverRequestDTO requestDTO){
        return driverService.updateDriver(id, requestDTO);
    }

    @Operation(summary = "Delete a driver",
                description = "Deletes a driver when there are no assignment linked to it.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable Long id){
        driverService.deleteDriver(id);
    }
}
