package com.jose.texaslogistics.driver;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
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
                .orElseThrow(()-> new DriverNotFoundException(id));

        return new DriverResponseDTO(driver);
    }

    public DriverResponseDTO updateDriver(Long id, DriverRequestDTO resquestDTO){
        Driver existingDriver = driverRepository.findById(id)
                .orElseThrow(()-> new DriverNotFoundException(id));

        existingDriver.setName(resquestDTO.getName());
        existingDriver.setEmail(resquestDTO.getEmail());
        existingDriver.setPhone(resquestDTO.getPhone());
        existingDriver.setStatus(resquestDTO.getStatus());

        Driver updatedDriver = driverRepository.save(existingDriver);

        return new DriverResponseDTO(updatedDriver);
    }

    public void deleteDriver(Long id) {
        Driver existingDriver = driverRepository.findById(id)
                .orElseThrow(()-> new DriverNotFoundException(id));

        driverRepository.delete(existingDriver);
    }
}
