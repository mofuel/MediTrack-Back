package com.MediTrack.domain.repository;

import com.MediTrack.domain.dto.ClinicalShiftsDTO;
import java.util.List;
import java.util.Optional;

public interface ClinicalShiftsRepository {

    ClinicalShiftsDTO save(ClinicalShiftsDTO dto);

    Optional<ClinicalShiftsDTO> findById(Long id);

    Optional<ClinicalShiftsDTO> findByNombre(String nombre);

    List<ClinicalShiftsDTO> findAll();

    void deleteById(Long id);
}
