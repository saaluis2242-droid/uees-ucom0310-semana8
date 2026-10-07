package edu.uees.reservas.api;

import edu.uees.reservas.domain.Reserva;
import edu.uees.reservas.service.ReservaService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de la capa web. Solo se levanta el Controller y el Service se reemplaza
 * por un doble (@MockitoBean): aquí se prueba que el Controller traduzca bien HTTP
 * y que no tenga lógica de negocio propia.
 */
@WebMvcTest(ReservaController.class)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservaService service;

    @Test
    void saludRespondeApiActiva() throws Exception {
        mockMvc.perform(get("/api/reservas/salud"))
                .andExpect(status().isOk())
                .andExpect(content().string("API activa"));
    }

    @Test
    void puedeCancelarDelegaEnElServiceYDevuelveSuRespuesta() throws Exception {
        // Arrange
        when(service.puedeCancelar(2)).thenReturn(true);

        // Act + Assert
        mockMvc.perform(get("/api/reservas/puede-cancelar").param("horas", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
        verify(service).puedeCancelar(2);
    }

    @Test
    void puedeCancelarSinParametroHorasRespondeBadRequest() throws Exception {
        mockMvc.perform(get("/api/reservas/puede-cancelar"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void puedeCancelarConHorasNoNumericasRespondeBadRequest() throws Exception {
        mockMvc.perform(get("/api/reservas/puede-cancelar").param("horas", "dos"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void crearReservaValidaResponde201ConLaReserva() throws Exception {
        // Arrange
        when(service.crear("R-001", "NORMAL")).thenReturn(new Reserva("R-001", "NORMAL"));
        String json = "{\"id\":\"R-001\",\"tipo\":\"NORMAL\"}";

        // Act + Assert
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("R-001"))
                .andExpect(jsonPath("$.tipo").value("NORMAL"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));
    }

    @Test
    void crearConIdVacioRespondeBadRequestYNoLlegaAlService() throws Exception {
        // Arrange
        String json = "{\"id\":\"\",\"tipo\":\"NORMAL\"}";

        // Act + Assert
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(service, never()).crear(any(), any());
    }

    @Test
    void crearSinTipoRespondeBadRequest() throws Exception {
        // Arrange
        String json = "{\"id\":\"R-002\"}";

        // Act + Assert
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
        verify(service, never()).crear(any(), any());
    }

    @Test
    void buscarReservaExistenteResponde200() throws Exception {
        // Arrange
        when(service.buscar("R-001")).thenReturn(new Reserva("R-001", "VIP"));

        // Act + Assert
        mockMvc.perform(get("/api/reservas/R-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("R-001"))
                .andExpect(jsonPath("$.tipo").value("VIP"));
    }

    /**
     * Reto: documenta el comportamiento ACTUAL. Como nadie maneja la excepción,
     * esta se escapa del Controller (en el servidor real termina en un 500).
     * Cuando se agregue un @RestControllerAdvice, esta prueba debe cambiar a esperar 404.
     */
    @Test
    void buscarIdInexistenteHoyNoTieneManejoHttpDeErrores() {
        // Arrange
        when(service.buscar("R-999")).thenThrow(new IllegalArgumentException("Reserva no encontrada"));

        // Act
        ServletException ex = assertThrows(
                ServletException.class,
                () -> mockMvc.perform(get("/api/reservas/R-999")));

        // Assert
        assertInstanceOf(IllegalArgumentException.class, ex.getCause());
    }
}
