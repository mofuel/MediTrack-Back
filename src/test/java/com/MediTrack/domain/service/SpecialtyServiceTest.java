package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.SpecialtyDTO;
import com.MediTrack.domain.repository.SpecialtyRepository;
import com.MediTrack.persistance.mapper.SpecialtyMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialtyServiceTest {

    @Mock
    private SpecialtyRepository specialtyRepository;

    @Mock
    private SpecialtyMapper specialtyMapper;

    @InjectMocks
    private SpecialtyService specialtyService;

    private SpecialtyDTO specialtyDTO;

    @BeforeEach
    void setUp() {
        specialtyDTO = new SpecialtyDTO();
        specialtyDTO.setId(1L);
        specialtyDTO.setNombre("Cardiología");
    }

    @Test
    void testSave_NuevaEspecialidad() {
        // Arrange
        when(specialtyRepository.existsByNombre("Cardiología")).thenReturn(false);
        when(specialtyRepository.save(specialtyDTO)).thenReturn(specialtyDTO);

        // Act
        SpecialtyDTO result = specialtyService.save(specialtyDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Cardiología", result.getNombre());
        verify(specialtyRepository, times(1)).save(specialtyDTO);
    }

    @Test
    void testSave_EspecialidadYaExiste() {
        when(specialtyRepository.existsByNombre("Cardiología")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> specialtyService.save(specialtyDTO),
                "La especialidad ya existe");
    }

    @Test
    void testGetById_Existe() {
        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(specialtyDTO));

        Optional<SpecialtyDTO> result = specialtyService.getById(1L);

        assertTrue(result.isPresent());
        assertEquals("Cardiología", result.get().getNombre());
    }

    @Test
    void testGetByNombre() {
        when(specialtyRepository.findByNombre("Cardiología"))
                .thenReturn(Optional.of(specialtyDTO));

        Optional<SpecialtyDTO> result = specialtyService.getByNombre("Cardiología");

        assertTrue(result.isPresent());
        assertEquals("Cardiología", result.get().getNombre());
    }

    @Test
    void testGetAll() {
        when(specialtyRepository.findAll()).thenReturn(List.of(specialtyDTO));

        List<SpecialtyDTO> result = specialtyService.getAll();

        assertEquals(1, result.size());
        assertEquals("Cardiología", result.get(0).getNombre());
    }

    @Test
    void testDelete_Existe() {
        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(specialtyDTO));

        boolean result = specialtyService.delete(1L);

        assertTrue(result);
        verify(specialtyRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NoExiste() {
        when(specialtyRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = specialtyService.delete(1L);

        assertFalse(result);
        verify(specialtyRepository, never()).deleteById(anyLong());
    }

    @Test
    void testUpdate_Existe() {
        SpecialtyDTO updatedDTO = new SpecialtyDTO();
        updatedDTO.setNombre("Neurología");

        when(specialtyRepository.findById(1L)).thenReturn(Optional.of(specialtyDTO));
        when(specialtyRepository.save(any(SpecialtyDTO.class))).thenReturn(updatedDTO);

        SpecialtyDTO result = specialtyService.update(1L, updatedDTO);

        assertNotNull(result);
        assertEquals("Neurología", result.getNombre());
    }

    @Test
    void testUpdate_NoExiste() {
        SpecialtyDTO updatedDTO = new SpecialtyDTO();
        updatedDTO.setNombre("Dermatología");

        when(specialtyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> specialtyService.update(1L, updatedDTO),
                "Especialidad no encontrada");
    }
}
