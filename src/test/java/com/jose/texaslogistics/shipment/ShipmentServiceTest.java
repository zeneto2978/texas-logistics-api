package com.jose.texaslogistics.shipment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private ShipmentService shipmentService;

    private Shipment shipmentPendente;
    private ShipmentRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        shipmentPendente = new Shipment();
        shipmentPendente.setOrigin("Austin");
        shipmentPendente.setDestination("Dallas");
        shipmentPendente.setDescription("Eletrônicos");
        shipmentPendente.setStatus(ShipmentStatus.PENDING);

        requestDTO = new ShipmentRequestDTO();
        requestDTO.setOrigin("Austin");
        requestDTO.setDestination("Dallas");
        requestDTO.setDescription("Eletrônicos");
    }

    @Test
    @DisplayName("Deve criar um shipment e retornar o DTO correspondente")
    void shouldCreateShipment() {
        // Arrange
        when(shipmentRepository.save(any(Shipment.class)))
                .thenReturn(shipmentPendente);

        // Act
        ShipmentResponseDTO response = shipmentService.createShipment(requestDTO);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getOrigin()).isEqualTo("Austin");
        assertThat(response.getDestination()).isEqualTo("Dallas");
        assertThat(response.getStatus()).isEqualTo(ShipmentStatus.PENDING);

        verify(shipmentRepository).save(any(Shipment.class));
    }

    @Test
    @DisplayName("Deve retornar o shipment quando o ID existe")
    void shouldReturnShipmentWhenIdExists() {
        // Arrange
        when(shipmentRepository.findById(1L))
                .thenReturn(Optional.of(shipmentPendente));

        // Act
        ShipmentResponseDTO response = shipmentService.getShipmentById(1L);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getOrigin()).isEqualTo("Austin");

        verify(shipmentRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ShipmentNotFoundException quando o ID não existe")
    void shouldThrowExceptionWhenIdDoesNotExist() {
        // Arrange
        when(shipmentRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> shipmentService.getShipmentById(999L))
                .isInstanceOf(ShipmentNotFoundException.class)
                .hasMessage("Shipment not found with id: 999");

        verify(shipmentRepository).findById(999L);
    }

    @Test
    @DisplayName("Deve atualizar um shipment existente")
    void shouldUpdateShipment() {
        // Arrange
        ShipmentRequestDTO updateRequest = new ShipmentRequestDTO();
        updateRequest.setOrigin("Houston");
        updateRequest.setDestination("San Antonio");
        updateRequest.setDescription("Móveis");

        when(shipmentRepository.findById(1L))
                .thenReturn(Optional.of(shipmentPendente));
        when(shipmentRepository.save(any(Shipment.class)))
                .thenReturn(shipmentPendente);

        // Act
        ShipmentResponseDTO response = shipmentService.updateShipment(1L, updateRequest);

        // Assert
        assertThat(response.getOrigin()).isEqualTo("Houston");
        assertThat(response.getDestination()).isEqualTo("San Antonio");
        assertThat(response.getDescription()).isEqualTo("Móveis");

        verify(shipmentRepository).findById(1L);
        verify(shipmentRepository).save(shipmentPendente);
    }

    @Test
    @DisplayName("Deve lançar ShipmentNotFoundException ao tentar atualizar um ID inexistente")
    void shouldThrowExceptionWhenUpdatingNonExistentShipment() {
        // Arrange
        when(shipmentRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> shipmentService.updateShipment(999L, requestDTO))
                .isInstanceOf(ShipmentNotFoundException.class)
                .hasMessage("Shipment not found with id: 999");
    }

    @Test
    @DisplayName("Deve deletar um shipment existente")
    void shouldDeleteShipment() {
        // Arrange
        when(shipmentRepository.existsById(1L)).thenReturn(true);

        // Act
        shipmentService.deleteShipment(1L);

        // Assert
        verify(shipmentRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar ShipmentNotFoundException ao tentar deletar um ID inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentShipment() {
        // Arrange
        when(shipmentRepository.existsById(999L)).thenReturn(false);

        // Act + Assert
        assertThatThrownBy(() -> shipmentService.deleteShipment(999L))
                .isInstanceOf(ShipmentNotFoundException.class)
                .hasMessage("Shipment not found with id: 999");
    }

    @Test
    @DisplayName("Deve retornar shipments filtrados por status")
    void shouldReturnShipmentsFilteredByStatus() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Shipment> page = new PageImpl<>(List.of(shipmentPendente));

        when(shipmentRepository.findByStatus(ShipmentStatus.PENDING, pageable))
                .thenReturn(page);

        // Act
        Page<ShipmentResponseDTO> response =
                shipmentService.getShipmentByStatus(ShipmentStatus.PENDING, pageable);

        // Assert
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getContent().get(0).getStatus())
                .isEqualTo(ShipmentStatus.PENDING);

        verify(shipmentRepository).findByStatus(ShipmentStatus.PENDING, pageable);
    }
}
