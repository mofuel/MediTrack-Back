package com.MediTrack.domain.repository;

import com.MediTrack.domain.dto.MedicProfileDTO;

import java.util.List;
import java.util.Optional;

public interface MedicProfileRepository {

    MedicProfileDTO save(MedicProfileDTO dto);

    Optional<MedicProfileDTO> findById(Long id);

    Optional<MedicProfileDTO> findByCodigoUsuario(String codigoUsuario);

    List<MedicProfileDTO> findAll();

    boolean existsByCodigoUsuario(String codigoUsuario);

    void delete(Long id);
}
