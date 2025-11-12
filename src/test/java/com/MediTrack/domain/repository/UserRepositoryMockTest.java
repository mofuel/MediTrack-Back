package com.MediTrack.domain.repository;

import com.MediTrack.persistance.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryMockTest {

    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);

        user = new User();
        user.setCodigo("U001");
        user.setNombre("Miguel");
        user.setEmail("miguel@example.com");

        when(userRepository.findByEmail("miguel@example.com"))
                .thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("miguel@example.com"))
                .thenReturn(true);
        when(userRepository.count())
                .thenReturn(5L);
    }

    @Test
    void testFindByEmail() {
        Optional<User> found = userRepository.findByEmail("miguel@example.com");
        assertTrue(found.isPresent());
        assertEquals("Miguel", found.get().getNombre());
    }

    @Test
    void testExistsByEmail() {
        assertTrue(userRepository.existsByEmail("miguel@example.com"));
    }

    @Test
    void testCount() {
        assertEquals(5, userRepository.count());
    }
}
