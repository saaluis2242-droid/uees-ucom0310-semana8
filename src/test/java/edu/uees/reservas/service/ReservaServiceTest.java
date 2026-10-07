package edu.uees.reservas.service;

import edu.uees.reservas.domain.EstadoReserva;
import edu.uees.reservas.domain.Reserva;
import edu.uees.reservas.repository.ReservaRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservaServiceTest {

    private final ReservaRepository repository = mock(ReservaRepository.class);
    private final ReservaService service = new ReservaService(repository);

    // ---------- Regla de cancelación ----------

    @Test
    void dosHorasPermitenCancelar() {
        assertTrue(service.puedeCancelar(2));
    }

    @Test
    void unaHoraNoPermiteCancelar() {
        assertFalse(service.puedeCancelar(1));
    }

    @Test
    void tresHorasPermitenCancelar() {
        // Arrange
        int horas = 3;
        // Act
        boolean resultado = service.puedeCancelar(horas);
        // Assert
        assertTrue(resultado);
    }

    @Test
    void ceroHorasNoPermitenCancelar() {
        // Arrange
        int horas = 0;
        // Act
        boolean resultado = service.puedeCancelar(horas);
        // Assert
        assertFalse(resultado);
    }

    @Test
    void horasNegativasNoPermitenCancelar() {
        // Arrange
        int horas = -1;
        // Act
        boolean resultado = service.puedeCancelar(horas);
        // Assert
        assertFalse(resultado);
    }

    // ---------- Crear, buscar y confirmar ----------

    @Test
    void crearGuardaUnaReservaPendiente() {
        // Arrange (el repositorio devuelve lo mismo que recibe)
        when(repository.guardar(any())).thenAnswer(invocacion -> invocacion.getArgument(0));

        // Act
        Reserva reserva = service.crear("R-001", "VIP");

        // Assert
        assertEquals("R-001", reserva.getId());
        assertEquals("VIP", reserva.getTipo());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
        verify(repository).guardar(reserva);
    }

    @Test
    void crearConIdVacioLanzaExcepcionYNoGuarda() {
        // Arrange
        String idVacio = "  ";

        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.crear(idVacio, "NORMAL"));

        // Assert
        assertEquals("Id obligatorio", ex.getMessage());
        verify(repository, never()).guardar(any());
    }

    @Test
    void crearConIdRepetidoLanzaReservaDuplicadaYNoGuarda() {
        // Arrange (ya existe una reserva con ese id)
        when(repository.buscarPorId("R-001")).thenReturn(Optional.of(new Reserva("R-001", "NORMAL")));

        // Act
        ReservaDuplicadaException ex = assertThrows(
                ReservaDuplicadaException.class,
                () -> service.crear("R-001", "VIP"));

        // Assert
        assertEquals("Ya existe una reserva con id: R-001", ex.getMessage());
        verify(repository, never()).guardar(any());
    }

    @Test
    void buscarIdExistenteDevuelveLaReserva() {
        // Arrange
        Reserva guardada = new Reserva("R-002", "NORMAL");
        when(repository.buscarPorId("R-002")).thenReturn(Optional.of(guardada));

        // Act
        Reserva encontrada = service.buscar("R-002");

        // Assert
        assertSame(guardada, encontrada);
    }

    @Test
    void buscarIdInexistenteLanzaReservaNoEncontrada() {
        // Arrange
        when(repository.buscarPorId("R-999")).thenReturn(Optional.empty());

        // Act
        ReservaNoEncontradaException ex = assertThrows(
                ReservaNoEncontradaException.class,
                () -> service.buscar("R-999"));

        // Assert
        assertEquals("Reserva no encontrada: R-999", ex.getMessage());
    }

    @Test
    void confirmarCambiaElEstadoYGuardaLaReserva() {
        // Arrange
        Reserva reserva = new Reserva("R-003", "ESTUDIANTE");
        when(repository.buscarPorId("R-003")).thenReturn(Optional.of(reserva));
        when(repository.guardar(reserva)).thenReturn(reserva);

        // Act
        Reserva confirmada = service.confirmar("R-003");

        // Assert
        assertEquals(EstadoReserva.CONFIRMADA, confirmada.getEstado());
        verify(repository).guardar(reserva);
    }
}
