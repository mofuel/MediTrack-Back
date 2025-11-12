package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.MedicProfileDTO;
import com.MediTrack.domain.repository.MedicProfileRepository;
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
class MedicProfileServiceTest {

    @Mock
    private MedicProfileRepository medicProfileRepository;

    @InjectMocks
    private MedicProfileService medicProfileService;

    @Test
    void testSave() {
        // Arrange
        MedicProfileDTO dto = new MedicProfileDTO();
        dto.setId(1L);
        dto.setCodigoUsuario("U001");

        when(medicProfileRepository.save(dto)).thenReturn(dto);

        // Act
        MedicProfileDTO result = medicProfileService.save(dto);

        // Assert
        assertNotNull(result);
        assertEquals("U001", result.getCodigoUsuario());
    }

    @Test
    void testFindById() {
        MedicProfileDTO dto = new MedicProfileDTO();
        dto.setId(1L);
        when(medicProfileRepository.findById(1L)).thenReturn(Optional.of(dto));

        Optional<MedicProfileDTO> result = medicProfileService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testFindByCodigoUsuario() {
        MedicProfileDTO dto = new MedicProfileDTO();
        dto.setCodigoUsuario("U002");

        when(medicProfileRepository.findByCodigoUsuario("U002"))
                .thenReturn(Optional.of(dto));

        Optional<MedicProfileDTO> result = medicProfileService.findByCodigoUsuario("U002");

        assertTrue(result.isPresent());
        assertEquals("U002", result.get().getCodigoUsuario());
    }

    @Test
    void testFindAll() {
        MedicProfileDTO dto = new MedicProfileDTO();
        dto.setCodigoUsuario("U003");

        when(medicProfileRepository.findAll()).thenReturn(List.of(dto));

        List<MedicProfileDTO> result = medicProfileService.findAll();

        assertEquals(1, result.size());
        assertEquals("U003", result.get(0).getCodigoUsuario());
    }

    @Test
    void testFindByEspecialidadId() {
        MedicProfileDTO dto = new MedicProfileDTO();
        dto.setCodigoUsuario("U004");

        when(medicProfileRepository.findByEspecialidadId(10L)).thenReturn(List.of(dto));

        List<MedicProfileDTO> result = medicProfileService.findByEspecialidadId(10L);

        assertFalse(result.isEmpty());
        assertEquals("U004", result.get(0).getCodigoUsuario());
    }

    @Test
    void testDeleteById() {
        // Act
        medicProfileService.deleteById(5L);

        // Assert
        verify(medicProfileRepository, times(1)).deleteById(5L);
    }
}
