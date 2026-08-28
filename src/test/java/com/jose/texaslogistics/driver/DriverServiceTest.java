package com.jose.texaslogistics.driver;

import com.jose.texaslogistics.*;
import com.jose.texaslogistics.assignment.AssignmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    private DriverService driverService;

    @Mock
    private AssignmentRepository assignmentRepository;

    @BeforeEach
    void setUp() {
        driverService = new DriverService(
                driverRepository,
                assignmentRepository);
    }

    @Test
    void shouldReturnDriverWhenDriveExists() {
        Driver driver = new Driver();

        driver.setName("Jose Mota");
        driver.setEmail("jose@email.com");
        driver.setPhone("5129999999");
        driver.setStatus(DriverStatus.AVAILABLE);

        when(driverRepository.findById(1L))
                .thenReturn(Optional.of(driver));

        DriverResponseDTO response =
                driverService.getDriverById(1L);

        assertNotNull(response);

        assertEquals(
                "Jose Mota",
                response.getName()
        );

        verify(driverRepository, times(1))
                .findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDriverDoesNotExist() {

        when(driverRepository.findById(999L))
                .thenReturn(Optional.empty());

        DriverNotFoundException exception =
                assertThrows(
                        DriverNotFoundException.class,
                        () -> driverService.getDriverById(999L)
                );

        assertEquals(
                "Driver not found with id: 999",
                exception.getMessage()
        );

        verify(driverRepository, times(1))
                .findById(999L);
    }
}
