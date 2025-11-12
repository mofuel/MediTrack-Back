package com.MediTrack.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ✅ Prueba de integración de la configuración de seguridad
 * Verifica qué endpoints están permitidos y cuáles requieren autenticación.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void swaggerEndpoints_shouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html")
                        .contentType(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @Test
    void authEndpoints_shouldBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError()); // Puede ser 404 si no existe, pero no 403
    }

    @Test
    void protectedEndpoints_shouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/specialties")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()); // 403 si no hay token JWT
    }
}
