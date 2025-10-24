package com.MediTrack.domain.repository;

import com.MediTrack.domain.dto.SpecialtyDTO;
import java.util.List;
import java.util.Optional;

public interface SpecialtyRepository {

    SpecialtyDTO save(SpecialtyDTO specialty);

    Optional<SpecialtyDTO> findById(Long id);

    Optional<SpecialtyDTO> findByNombre(String nombre);

    List<SpecialtyDTO> findAll();

    boolean existsByNombre(String nombre);

    void deleteById(Long id);
}
