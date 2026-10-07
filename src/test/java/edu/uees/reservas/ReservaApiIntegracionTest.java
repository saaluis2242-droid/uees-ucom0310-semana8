package edu.uees.reservas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Prueba de integración: levanta la aplicación completa (Controller, Service,
 * repositorio real en memoria y manejador de errores) sin ningún mock.
 * Recorre el flujo principal tal como lo haría un cliente de la API.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ReservaApiIntegracionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void flujoCompletoCrearBuscarDuplicarYNoEncontrar() throws Exception {
        String json = "{\"id\":\"R-100\",\"tipo\":\"VIP\"}";

        // 1. La API está activa
        mockMvc.perform(get("/api/reservas/salud"))
                .andExpect(status().isOk())
                .andExpect(content().string("API activa"));

        // 2. Se crea la reserva
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("PENDIENTE"));

        // 3. Se puede buscar porque quedó guardada en el repositorio real
        mockMvc.perform(get("/api/reservas/R-100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("VIP"));

        // 4. Crearla otra vez con el mismo id responde 409
        mockMvc.perform(post("/api/reservas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict());

        // 5. Un id que no existe responde 404
        mockMvc.perform(get("/api/reservas/R-999"))
                .andExpect(status().isNotFound());
    }
}
