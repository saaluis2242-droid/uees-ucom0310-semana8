package edu.uees.reservas.api;

import edu.uees.reservas.domain.Reserva;
import edu.uees.reservas.service.ReservaNoEncontradaException;
import edu.uees.reservas.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void buscarIdInexistenteResponde404ConMensaje() throws Exception {
        // Arrange
        when(service.buscar("R-999")).thenThrow(new ReservaNoEncontradaException("R-999"));

        // Act + Assert
        mockMvc.perform(get("/api/reservas/R-999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value(404))
                .andExpect(jsonPath("$.mensaje").value("Reserva no encontrada: R-999"));
    }
}
