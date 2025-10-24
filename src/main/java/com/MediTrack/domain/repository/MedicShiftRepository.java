package com.MediTrack.domain.repository;

import com.MediTrack.domain.dto.MedicShiftDTO;

import java.util.List;

public interface MedicShiftRepository {
    MedicShiftDTO save(MedicShiftDTO dto);

    List<MedicShiftDTO> findByPerfilId(Long perfilId);

    List<MedicShiftDTO> findByTurnoId(Long turnoId);

    boolean existsByPerfilAndDia(Long perfilId, short diaSemana);

    void delete(Long id);
}
