package com.jose.texaslogistics.driver;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<DriverResponseDTO> getAllDrivers(){
        return driverService.getAllDrivers();
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
