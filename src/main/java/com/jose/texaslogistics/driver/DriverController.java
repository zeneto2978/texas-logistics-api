package com.jose.texaslogistics.driver;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService){
        this.driverService = driverService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverResponseDTO createDriver(@Valid @RequestBody DriverRequestDTO requestDTO){
        return driverService.createDriver(requestDTO);
    }

    @GetMapping
    public Page<DriverResponseDTO> getAllDrivers(
            @RequestParam(required = false) DriverStatus status,
            Pageable pageable) {
        if (status != null) {
            return driverService.getDriversByStatus(status, pageable);
        }

        return driverService.getAllDrivers(pageable);
    }

    @GetMapping("/{id}")
    public DriverResponseDTO getDriverById(@PathVariable Long id){
        return driverService.getDriverById(id);
    }

    @PutMapping("/{id}")
    public DriverResponseDTO updateDriver(@PathVariable Long id,
                                          @Valid
                                          @RequestBody DriverRequestDTO requestDTO){
        return driverService.updateDriver(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable Long id){
        driverService.deleteDriver(id);
    }
}
