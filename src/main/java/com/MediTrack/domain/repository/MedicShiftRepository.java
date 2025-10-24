package com.MediTrack.domain.repository;

import com.MediTrack.domain.dto.MedicShiftDTO;

import java.util.List;
import java.util.Optional;

public interface MedicShiftRepository {
    MedicShiftDTO save(MedicShiftDTO dto);

    Optional<MedicShiftDTO> findById(Long id);

    List<MedicShiftDTO> findAll();

    List<MedicShiftDTO> findByPerfilId(Long perfilId);

    List<MedicShiftDTO> findByTurnoId(Long turnoId);

    boolean existsByPerfilAndDia(Long perfilId, short diaSemana);

    void delete(Long id);
}
