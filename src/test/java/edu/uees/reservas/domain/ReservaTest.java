package edu.uees.reservas.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Agregada en la auditoría: JaCoCo mostraba en amarillo el constructor de Reserva
 * (id nulo y tipo nulo nunca se probaban) y el método cancelar() en rojo.
 */
class ReservaTest {

    @Test
    void reservaConIdNuloLanzaExcepcion() {
        // Act
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new Reserva(null, "NORMAL"));

        // Assert
        assertEquals("Id obligatorio", ex.getMessage());
    }

    @Test
    void tipoNuloSeAsumeNormalYQuedaPendiente() {
        // Act
        Reserva reserva = new Reserva("R-010", null);

        // Assert
        assertEquals("NORMAL", reserva.getTipo());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());
    }

    @Test
    void cancelarCambiaElEstadoACancelada() {
        // Arrange
        Reserva reserva = new Reserva("R-011", "VIP");

        // Act
        reserva.cancelar();

        // Assert
        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
    }
}
