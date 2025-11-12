package com.MediTrack.domain.repository;

import com.MediTrack.domain.dto.MedicProfileDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicProfileRepositoryMockTest {

    private MedicProfileRepository medicProfileRepository;
    private MedicProfileDTO medicProfileDTO;

    @BeforeEach
    void setUp() {
        // 🧱 Crear mock del repositorio
        medicProfileRepository = Mockito.mock(MedicProfileRepository.class);

        // 🧱 Crear datos simulados
        medicProfileDTO = new MedicProfileDTO();
        medicProfileDTO.setId(1L);
        medicProfileDTO.setCodigoUsuario("U001");
        medicProfileDTO.setFechaCreacion(LocalDateTime.now());

        // 🎯 Configurar comportamiento del mock
        when(medicProfileRepository.findById(1L))
                .thenReturn(Optional.of(medicProfileDTO));
        when(medicProfileRepository.findByCodigoUsuario("U001"))
                .thenReturn(Optional.of(medicProfileDTO));
        when(medicProfileRepository.findAll())
                .thenReturn(List.of(medicProfileDTO));
        when(medicProfileRepository.existsByCodigoUsuario("U001"))
                .thenReturn(true);
    }

    @Test
    void testFindById() {
        Optional<MedicProfileDTO> result = medicProfileRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("U001", result.get().getCodigoUsuario());
        verify(medicProfileRepository).findById(1L);
    }

    @Test
    void testFindByCodigoUsuario() {
        Optional<MedicProfileDTO> result = medicProfileRepository.findByCodigoUsuario("U001");

        assertTrue(result.isPresent());
        assertEquals("U001", result.get().getCodigoUsuario());
        verify(medicProfileRepository).findByCodigoUsuario("U001");
    }

    @Test
    void testFindAll() {
        List<MedicProfileDTO> result = medicProfileRepository.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(medicProfileRepository).findAll();
    }

    @Test
    void testExistsByCodigoUsuario() {
        assertTrue(medicProfileRepository.existsByCodigoUsuario("U001"));
        verify(medicProfileRepository).existsByCodigoUsuario("U001");
    }

    @Test
    void testDeleteById() {
        doNothing().when(medicProfileRepository).deleteById(1L);

        medicProfileRepository.deleteById(1L);

        verify(medicProfileRepository).deleteById(1L);
    }
}