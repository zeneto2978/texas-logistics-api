package com.jose.texaslogistics.driver;

import com.jose.texaslogistics.assignment.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final AssignmentRepository assignmentRepository;

    // O construtor agora recebe dois repositories.
    //
    // DriverRepository:
    // responsável por todas as operações do motorista no banco.
    //
    // AssignmentRepository:
    // usado apenas para verificar se o motorista
    // possui assignments antes de deletar.
    public DriverService(DriverRepository driverRepository,
                         AssignmentRepository assignmentRepository) {
        this.driverRepository = driverRepository;
        this.assignmentRepository = assignmentRepository;
    }

    public DriverResponseDTO createDriver(DriverRequestDTO requestDTO){
        Driver driver = new Driver();

        driver.setName(requestDTO.getName());
        driver.setEmail(requestDTO.getEmail());
        driver.setPhone(requestDTO.getPhone());
        driver.setStatus(requestDTO.getStatus());

        Driver savedDriver = driverRepository.save(driver);

        return new DriverResponseDTO(savedDriver);
    }

    public List<DriverResponseDTO> getAllDrivers() {
        return driverRepository.findAll()
                .stream()
                .map(DriverResponseDTO::new)
                .toList();
    }

    public DriverResponseDTO getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));

        return new DriverResponseDTO(driver);
    }

    public DriverResponseDTO updateDriver(Long id, DriverRequestDTO requestDTO){
        Driver existingDriver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));

        existingDriver.setName(requestDTO.getName());
        existingDriver.setEmail(requestDTO.getEmail());
        existingDriver.setPhone(requestDTO.getPhone());
        existingDriver.setStatus(requestDTO.getStatus());

        Driver updatedDriver = driverRepository.save(existingDriver);

        return new DriverResponseDTO(updatedDriver);
    }

    public void deleteDriver(Long id) {
        Driver existingDriver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));

        // Antes de deletar, verificamos se existe alguma Assignment
        // vinculada a esse motorista.
        //
        // Se existir, lançamos DriverHasAssignmentsException.
        // Essa exceção será capturada pelo DriverExceptionHandler
        // e retornará um 409 Conflict com mensagem clara.
        //
        // Isso evita que o banco lance um erro de foreign key (500)
        // e garante que a API responda de forma profissional.
        if (assignmentRepository.existsByDriverId(id)) {
            throw new DriverHasAssignmentsException(id);
        }

        driverRepository.delete(existingDriver);
    }
}