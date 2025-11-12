package com.MediTrack.persistance.entity;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ✅ Prueba unitaria para la entidad User.
 * Verifica que los getters, setters y el estado inicial funcionen correctamente.
 */
class UserTest {

    @Test
    void testGettersAndSetters() {
        User user = new User();

        user.setCodigo("U0001");
        user.setNombre("Juan");
        user.setApellido("Pérez");
        user.setDni("12345678");
        user.setSexo("Masculino");
        user.setEmail("juan@example.com");
        user.setTelefono("987654321");
        user.setRol("ROLE_PACIENTE");
        user.setActivo(true);
        user.setPassword("password123");

        assertEquals("U0001", user.getCodigo());
        assertEquals("Juan", user.getNombre());
        assertEquals("Pérez", user.getApellido());
        assertEquals("12345678", user.getDni());
        assertEquals("Masculino", user.getSexo());
        assertEquals("juan@example.com", user.getEmail());
        assertEquals("987654321", user.getTelefono());
        assertEquals("ROLE_PACIENTE", user.getRol());
        assertTrue(user.isActivo());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void testDefaultActivoIsTrue() {
        User user = new User();
        assertTrue(user.isActivo(), "El usuario debería ser activo por defecto");
    }

    @Test
    void testEqualsBetweenTwoUsers() {
        User u1 = new User();
        u1.setCodigo("U0001");

        User u2 = new User();
        u2.setCodigo("U0001");

        // Como no sobreescribiste equals/hashCode, no serán iguales aunque el código sea igual
        assertNotEquals(u1, u2);
    }
}
