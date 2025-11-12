package com.MediTrack.domain.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDTOTest {

    @Test
    void testGettersAndSetters() {
        UserDTO userDTO = new UserDTO();

        userDTO.setNombre("Miguel");
        userDTO.setApellido("Monzón");
        userDTO.setDni("12345678");
        userDTO.setSexo("Masculino");
        userDTO.setEmail("miguel@example.com");
        userDTO.setTelefono("987654321");
        userDTO.setRol("ROLE_ADMIN");
        userDTO.setPassword("secure123");
        userDTO.setActivo(true);

        assertEquals("Miguel", userDTO.getNombre());
        assertEquals("Monzón", userDTO.getApellido());
        assertEquals("12345678", userDTO.getDni());
        assertEquals("Masculino", userDTO.getSexo());
        assertEquals("miguel@example.com", userDTO.getEmail());
        assertEquals("987654321", userDTO.getTelefono());
        assertEquals("ROLE_ADMIN", userDTO.getRol());
        assertEquals("secure123", userDTO.getPassword());
        assertTrue(userDTO.isActivo());
    }
}
