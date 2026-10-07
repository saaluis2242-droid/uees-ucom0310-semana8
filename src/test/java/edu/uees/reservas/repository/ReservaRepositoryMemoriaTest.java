package edu.uees.reservas.repository;

import edu.uees.reservas.domain.Reserva;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Agregada en la auditoría: JaCoCo mostraba esta clase en 0 % porque todas las
 * pruebas usaban un mock del repositorio. Aquí se prueba la implementación real,
 * la misma que usa la aplicación cuando corre.
 */
class ReservaRepositoryMemoriaTest {

    private final ReservaRepositoryMemoria repository = new ReservaRepositoryMemoria();

    @Test
    void guardarYBuscarDevuelveLaMismaReserva() {
        // Arrange
        Reserva reserva = new Reserva("R-001", "NORMAL");

        // Act
        Reserva guardada = repository.guardar(reserva);
        Optional<Reserva> encontrada = repository.buscarPorId("R-001");

        // Assert
        assertSame(reserva, guardada);
        assertTrue(encontrada.isPresent());
        assertSame(reserva, encontrada.get());
    }

    @Test
    void buscarIdInexistenteDevuelveOptionalVacio() {
        // Act
        Optional<Reserva> encontrada = repository.buscarPorId("R-999");

        // Assert
        assertTrue(encontrada.isEmpty());
    }

    @Test
    void guardarConIdRepetidoReemplazaLaReservaAnterior() {
        // Arrange (comportamiento actual: no hay control de duplicados)
        repository.guardar(new Reserva("R-002", "NORMAL"));
        Reserva segunda = new Reserva("R-002", "VIP");

        // Act
        repository.guardar(segunda);

        // Assert
        assertEquals("VIP", repository.buscarPorId("R-002").orElseThrow().getTipo());
    }
}
