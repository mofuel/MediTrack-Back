package com.MediTrack.persistance.crud;

import com.MediTrack.persistance.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserCrudRepositoryTest {

    @Autowired
    private UserCrudRepository userCrudRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userCrudRepository.deleteAll();

        user1 = new User();
        user1.setCodigo("U001");
        user1.setNombre("Miguel");
        user1.setApellido("Monzón");
        user1.setDni("12345678");
        user1.setSexo("Masculino");
        user1.setEmail("miguel@example.com");
        user1.setTelefono("987654321");
        user1.setRol("ROLE_ADMIN");
        user1.setPassword("secure123");
        user1.setActivo(true);

        user2 = new User();
        user2.setCodigo("U002");
        user2.setNombre("Ana");
        user2.setApellido("López");
        user2.setDni("87654321");
        user2.setSexo("Femenino");
        user2.setEmail("ana@example.com");
        user2.setTelefono("912345678");
        user2.setRol("ROLE_USER");
        user2.setPassword("pass123");
        user2.setActivo(true);

        userCrudRepository.save(user1);
        userCrudRepository.save(user2);
    }

    @Test
    void testFindByEmail() {
        Optional<User> found = userCrudRepository.findByEmail("miguel@example.com");
        assertTrue(found.isPresent());
        assertEquals("Miguel", found.get().getNombre());
    }

    @Test
    void testFindByCodigo() {
        Optional<User> found = userCrudRepository.findByCodigo("U002");
        assertTrue(found.isPresent());
        assertEquals("Ana", found.get().getNombre());
    }

    @Test
    void testExistsByDni() {
        assertTrue(userCrudRepository.existsByDni("12345678"));
        assertFalse(userCrudRepository.existsByDni("00000000"));
    }

    @Test
    void testExistsByEmail() {
        assertTrue(userCrudRepository.existsByEmail("ana@example.com"));
        assertFalse(userCrudRepository.existsByEmail("noexiste@example.com"));
    }

    @Test
    void testFindByRol() {
        List<User> admins = userCrudRepository.findByRol("ROLE_ADMIN");
        assertEquals(1, admins.size());
        assertEquals("Miguel", admins.get(0).getNombre());
    }

    @Test
    void testDeleteByCodigo() {
        userCrudRepository.deleteByCodigo("U001");
        assertFalse(userCrudRepository.findByCodigo("U001").isPresent());
    }
}
