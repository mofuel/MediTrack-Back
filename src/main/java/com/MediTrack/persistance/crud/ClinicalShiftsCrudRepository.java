package com.MediTrack.persistance.crud;

import com.MediTrack.persistance.entity.ClinicalShifts;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad ClinicalShifts
 */

public interface ClinicalShiftsCrudRepository extends JpaRepository<ClinicalShifts, Long> {
    Optional<ClinicalShifts> findByNombre(String nombre);
}
