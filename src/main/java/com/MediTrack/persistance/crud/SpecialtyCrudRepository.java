package com.MediTrack.persistance.crud;

import com.MediTrack.persistance.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad Specialty
 */

public interface SpecialtyCrudRepository extends JpaRepository<Specialty, Long> {

    Optional<Specialty> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
