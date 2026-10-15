package com.jose.texaslogistics.driver;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
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

    @Operation(
            summary = "Create a new driver",
            description = "Registers a new driver in the logistics system.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Driver created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid driver data"
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverResponseDTO createDriver(@Valid @RequestBody DriverRequestDTO requestDTO){
        return driverService.createDriver(requestDTO);
    }

    @Operation(
            summary = "List drivers",
            description = "Returns drivers with pagination, sorting and optional status filtering.")
    // Endpoint GET /drivers.
// Permite consultar motoristas com filtro opcional,
// paginação e ordenação.
    @GetMapping
    public Page<DriverResponseDTO> getAllDrivers(

            // Filtra os motoristas pelo status informado.
            // Quando não informado, retorna todos os status.
            @RequestParam(required = false)
            DriverStatus status,

            // Faz o Swagger documentar page, size e sort
            // como parâmetros separados na URL.
            @ParameterObject
            Pageable pageable) {

        // Quando um status é informado, consulta somente
        // os motoristas que possuem esse status.
        if (status != null) {
            return driverService.getDriversByStatus(status, pageable);
        }

        // Sem filtro, consulta todos os motoristas,
        // respeitando a paginação e a ordenação.
        return driverService.getAllDrivers(pageable);
    }

    @Operation(
            summary = "Find driver by ID",
            description = "Returns a driver registered in the logistics system.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Driver found successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Driver not found"
            )
    })
    @GetMapping("/{id}")
    public DriverResponseDTO getDriverById(@PathVariable Long id){
        return driverService.getDriverById(id);
    }

    @Operation(
            summary = "Update a driver",
            description = "Updates the information of an existing driver.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Driver updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid driver data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Driver not found"
            )
    })
    @PutMapping("/{id}")
    public DriverResponseDTO updateDriver(@PathVariable Long id,
                                          @Valid
                                          @RequestBody DriverRequestDTO requestDTO){
        return driverService.updateDriver(id, requestDTO);
    }

    @Operation(
            summary = "Delete a driver",
            description = "Deletes a driver when there are no assignments linked to it.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Driver deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Driver not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Driver has linked assignments"
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable Long id){
        driverService.deleteDriver(id);
    }
}
