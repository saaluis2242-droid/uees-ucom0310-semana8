package edu.uees.reservas.service;

import edu.uees.reservas.repository.ReservaRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

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
}
