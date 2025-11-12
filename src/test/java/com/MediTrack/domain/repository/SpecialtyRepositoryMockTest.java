package com.MediTrack.domain.repository;

import com.MediTrack.domain.dto.SpecialtyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpecialtyRepositoryMockTest {

    private SpecialtyRepository specialtyRepository;
    private SpecialtyDTO specialty;

    @BeforeEach
    void setUp() {
        specialtyRepository = Mockito.mock(SpecialtyRepository.class);

        specialty = new SpecialtyDTO();
        specialty.setId(1L);
        specialty.setNombre("Cardiología");

        // Configuramos el comportamiento simulado (mock)
        when(specialtyRepository.findById(1L))
                .thenReturn(Optional.of(specialty));
        when(specialtyRepository.findByNombre("Cardiología"))
                .thenReturn(Optional.of(specialty));
        when(specialtyRepository.findAll())
                .thenReturn(List.of(specialty));
        when(specialtyRepository.existsByNombre("Cardiología"))
                .thenReturn(true);
    }

    @Test
    void testFindById() {
        Optional<SpecialtyDTO> found = specialtyRepository.findById(1L);
        assertTrue(found.isPresent());
        assertEquals("Cardiología", found.get().getNombre());
    }

    @Test
    void testFindByNombre() {
        Optional<SpecialtyDTO> found = specialtyRepository.findByNombre("Cardiología");
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    void testFindAll() {
        List<SpecialtyDTO> specialties = specialtyRepository.findAll();
        assertEquals(1, specialties.size());
        assertEquals("Cardiología", specialties.get(0).getNombre());
    }

    @Test
    void testExistsByNombre() {
        assertTrue(specialtyRepository.existsByNombre("Cardiología"));
    }

    @Test
    void testDeleteById() {
        specialtyRepository.deleteById(1L);
        verify(specialtyRepository, times(1)).deleteById(1L);
    }
}