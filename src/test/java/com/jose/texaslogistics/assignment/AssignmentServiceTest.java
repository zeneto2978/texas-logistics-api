// ─────────────────────────────────────────────────────────────────────────────
// ONDE ESTE ARQUIVO DEVE FICAR:
// src/test/java/com/jose/texaslogistics/assignment/AssignmentServiceTest.java
//
// ATENÇÃO: o package estava ERRADO na sua versão ("package assignment").
// O correto é o caminho completo abaixo.
// ─────────────────────────────────────────────────────────────────────────────
package com.jose.texaslogistics.assignment;

import com.jose.texaslogistics.driver.DriverBusyException;
import com.jose.texaslogistics.driver.DriverInactiveException;
import com.jose.texaslogistics.driver.DriverNotFoundException;
import com.jose.texaslogistics.driver.DriverRepository;
import com.jose.texaslogistics.driver.Driver;
import com.jose.texaslogistics.driver.DriverStatus;
import com.jose.texaslogistics.shipment.Shipment;
import com.jose.texaslogistics.shipment.ShipmentNotAssignableException;
import com.jose.texaslogistics.shipment.ShipmentNotInTransitException;
import com.jose.texaslogistics.shipment.ShipmentNotFoundException;
import com.jose.texaslogistics.shipment.ShipmentRepository;
import com.jose.texaslogistics.shipment.ShipmentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private AssignmentRepository assignmentRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private AssignmentService assignmentService;

    // ─────────────────────────────────────────────────────────────────────────
    // Objetos base que a maioria dos testes usa.
    // Criados no @BeforeEach para não repetir código.
    // ─────────────────────────────────────────────────────────────────────────
    private Driver driverDisponivel;
    private Shipment shipmentPendente;
    private Assignment assignment;
    private AssignmentRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Driver pronto para receber um shipment
        driverDisponivel = new Driver();
        driverDisponivel.setName("Jose Mota");
        driverDisponivel.setEmail("jose@email.com");
        driverDisponivel.setPhone("5129999999");
        driverDisponivel.setStatus(DriverStatus.AVAILABLE);

        // Shipment aguardando ser atribuído
        shipmentPendente = new Shipment();
        shipmentPendente.setOrigin("Austin");
        shipmentPendente.setDestination("Dallas");
        shipmentPendente.setDescription("Eletrônicos");
        shipmentPendente.setStatus(ShipmentStatus.PENDING);

        // Assignment que liga os dois — usado nos testes de completeAssignment
        assignment = new Assignment();
        assignment.setDriver(driverDisponivel);
        assignment.setShipment(shipmentPendente);

        // DTO como se viesse do corpo da requisição JSON
        requestDTO = new AssignmentRequestDTO();
        requestDTO.setDriverId(1L);
        requestDTO.setShipmentId(1L);
    }


    // =========================================================================
    // TESTES: createAssignment — o cenário feliz
    // =========================================================================

    @Test
    @DisplayName("createAssignment → deve criar assignment, marcar driver como BUSY e shipment como IN_TRANSIT")
    void createAssignment_deveCriarEAtualizarStatusesCorretamente() {

        // ── ARRANGE ───────────────────────────────────────────────────────────
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driverDisponivel));
        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipmentPendente));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);

        // ── ACT ───────────────────────────────────────────────────────────────
        AssignmentResponseDTO resultado = assignmentService.createAssignment(requestDTO);

        // ── ASSERT ────────────────────────────────────────────────────────────
        assertThat(resultado).isNotNull();

        // O service deve ter mudado o status do driver para BUSY
        assertThat(driverDisponivel.getStatus()).isEqualTo(DriverStatus.BUSY);

        // O service deve ter mudado o status do shipment para IN_TRANSIT
        assertThat(shipmentPendente.getStatus()).isEqualTo(ShipmentStatus.IN_TRANSIT);

        // A operação deve ter sido salva no banco
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
    }


    // =========================================================================
    // TESTES: createAssignment — regras de negócio do Driver
    // =========================================================================

    @Test
    @DisplayName("createAssignment → deve lançar DriverNotFoundException quando driver não existe")
    void createAssignment_deveLancarExcecao_quandoDriverNaoExiste() {

        // ARRANGE: banco não encontra o driver
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        // ASSERT + ACT
        assertThatThrownBy(() -> assignmentService.createAssignment(requestDTO))
                .isInstanceOf(DriverNotFoundException.class)
                .hasMessageContaining("1");

        // Se o driver não existe, nem deve tentar buscar o shipment
        verify(shipmentRepository, never()).findById(any());
        verify(assignmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("createAssignment → deve lançar DriverInactiveException quando driver está INACTIVE")
    void createAssignment_deveLancarExcecao_quandoDriverInativo() {

        // ── ARRANGE ───────────────────────────────────────────────────────────
        // Mudamos o status do driver para INACTIVE para testar essa regra
        driverDisponivel.setStatus(DriverStatus.INACTIVE);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driverDisponivel));

        // ── ASSERT + ACT ──────────────────────────────────────────────────────
        // O service deve recusar a criação porque motoristas inativos
        // não podem receber novos trabalhos
        assertThatThrownBy(() -> assignmentService.createAssignment(requestDTO))
                .isInstanceOf(DriverInactiveException.class);
        // ID é null em testes unitários (sem banco), verificamos só o tipo da exceção

        verify(assignmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("createAssignment → deve lançar DriverBusyException quando driver está BUSY")
    void createAssignment_deveLancarExcecao_quandoDriverOcupado() {

        // ── ARRANGE ───────────────────────────────────────────────────────────
        // Driver já está carregando outro shipment
        driverDisponivel.setStatus(DriverStatus.BUSY);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driverDisponivel));

        // ── ASSERT + ACT ──────────────────────────────────────────────────────
        assertThatThrownBy(() -> assignmentService.createAssignment(requestDTO))
                .isInstanceOf(DriverBusyException.class);
        // ID é null em testes unitários (sem banco), verificamos só o tipo da exceção

        verify(assignmentRepository, never()).save(any());
    }


    // =========================================================================
    // TESTES: createAssignment — regras de negócio do Shipment
    // =========================================================================

    @Test
    @DisplayName("createAssignment → deve lançar ShipmentNotFoundException quando shipment não existe")
    void createAssignment_deveLancarExcecao_quandoShipmentNaoExiste() {

        when(driverRepository.findById(1L)).thenReturn(Optional.of(driverDisponivel));
        when(shipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assignmentService.createAssignment(requestDTO))
                .isInstanceOf(ShipmentNotFoundException.class)
                .hasMessageContaining("1");

        verify(assignmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("createAssignment → deve lançar ShipmentNotAssignableException quando shipment não está PENDING")
    void createAssignment_deveLancarExcecao_quandoShipmentNaoEstaPendente() {

        // ── ARRANGE ───────────────────────────────────────────────────────────
        // Simulamos um shipment que já está em trânsito — não pode ser atribuído
        shipmentPendente.setStatus(ShipmentStatus.IN_TRANSIT);

        when(driverRepository.findById(1L)).thenReturn(Optional.of(driverDisponivel));
        when(shipmentRepository.findById(1L)).thenReturn(Optional.of(shipmentPendente));

        // ── ASSERT + ACT ──────────────────────────────────────────────────────
        assertThatThrownBy(() -> assignmentService.createAssignment(requestDTO))
                .isInstanceOf(ShipmentNotAssignableException.class);
        // ID é null em testes unitários (sem banco), verificamos só o tipo da exceção

        verify(assignmentRepository, never()).save(any());
    }


    // =========================================================================
    // TESTES: getAssignmentById
    // =========================================================================

    @Test
    @DisplayName("getAssignmentById → deve retornar assignment quando existe")
    void getAssignmentById_deveRetornarAssignment_quandoExiste() {

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));

        AssignmentResponseDTO resultado = assignmentService.getAssignmentById(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getDriverName()).isEqualTo("Jose Mota");
        assertThat(resultado.getShipmentOrigin()).isEqualTo("Austin");
    }

    @Test
    @DisplayName("getAssignmentById → deve lançar AssignmentNotFoundException quando não existe")
    void getAssignmentById_deveLancarExcecao_quandoNaoExiste() {

        when(assignmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assignmentService.getAssignmentById(99L))
                .isInstanceOf(AssignmentNotFoundException.class)
                .hasMessageContaining("99");
    }


    // =========================================================================
    // TESTES: completeAssignment
    // =========================================================================

    @Test
    @DisplayName("completeAssignment → deve marcar shipment como DELIVERED e driver como AVAILABLE")
    void completeAssignment_deveConcluirCorretamente() {

        // ── ARRANGE ───────────────────────────────────────────────────────────
        // Para completar, o shipment precisa estar IN_TRANSIT
        shipmentPendente.setStatus(ShipmentStatus.IN_TRANSIT);
        driverDisponivel.setStatus(DriverStatus.BUSY);

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));

        // ── ACT ───────────────────────────────────────────────────────────────
        AssignmentResponseDTO resultado = assignmentService.completeAssignment(1L);

        // ── ASSERT ────────────────────────────────────────────────────────────
        assertThat(resultado).isNotNull();

        // Depois de completar: shipment entregue, driver disponível novamente
        assertThat(shipmentPendente.getStatus()).isEqualTo(ShipmentStatus.DELIVERED);
        assertThat(driverDisponivel.getStatus()).isEqualTo(DriverStatus.AVAILABLE);
    }

    @Test
    @DisplayName("completeAssignment → deve lançar AssignmentNotFoundException quando assignment não existe")
    void completeAssignment_deveLancarExcecao_quandoAssignmentNaoExiste() {

        when(assignmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> assignmentService.completeAssignment(99L))
                .isInstanceOf(AssignmentNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("completeAssignment → deve lançar ShipmentNotInTransitException quando shipment não está IN_TRANSIT")
    void completeAssignment_deveLancarExcecao_quandoShipmentNaoEstaEmTransito() {

        // ── ARRANGE ───────────────────────────────────────────────────────────
        // O shipment ainda está PENDING — não pode ser completado
        shipmentPendente.setStatus(ShipmentStatus.PENDING);

        when(assignmentRepository.findById(1L)).thenReturn(Optional.of(assignment));

        // ── ASSERT + ACT ──────────────────────────────────────────────────────
        assertThatThrownBy(() -> assignmentService.completeAssignment(1L))
                .isInstanceOf(ShipmentNotInTransitException.class);
        // ID é null em testes unitários (sem banco), verificamos só o tipo da exceção

        // Os status não devem ter mudado
        assertThat(shipmentPendente.getStatus()).isEqualTo(ShipmentStatus.PENDING);
        assertThat(driverDisponivel.getStatus()).isEqualTo(DriverStatus.AVAILABLE);
    }


    // =========================================================================
    // TESTES: getAssignmentByDriver
    // =========================================================================

    @Test
    @DisplayName("getAssignmentByDriver → deve retornar assignments do driver quando ele existe")
    void getAssignmentByDriver_deveRetornarAssignments_quandoDriverExiste() {

        when(driverRepository.existsById(1L)).thenReturn(true);
        when(assignmentRepository.findByDriverId(1L)).thenReturn(List.of(assignment));

        List<AssignmentResponseDTO> resultado = assignmentService.getAssignmentByDriver(1L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getDriverName()).isEqualTo("Jose Mota");
    }

    @Test
    @DisplayName("getAssignmentByDriver → deve lançar DriverNotFoundException quando driver não existe")
    void getAssignmentByDriver_deveLancarExcecao_quandoDriverNaoExiste() {

        when(driverRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> assignmentService.getAssignmentByDriver(99L))
                .isInstanceOf(DriverNotFoundException.class)
                .hasMessageContaining("99");

        // Se o driver não existe, não faz sentido buscar assignments
        verify(assignmentRepository, never()).findByDriverId(any());
    }
}