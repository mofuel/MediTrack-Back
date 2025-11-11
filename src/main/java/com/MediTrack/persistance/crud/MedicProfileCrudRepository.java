package com.MediTrack.persistance.crud;

import com.MediTrack.persistance.entity.MedicProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad MedicProfile
 */

public interface MedicProfileCrudRepository extends JpaRepository<MedicProfile, Long> {

    Optional<MedicProfile> findByCodigoUsuario(String codigoUsuario);

    boolean existsByCodigoUsuario(String codigoUsuario);

    // Buscar médicos por ID de especialidad
    @Query("SELECT m FROM MedicProfile m JOIN m.especialidades e WHERE e.id = :especialidadId")
    List<MedicProfile> findByEspecialidadId(Long especialidadId);
}
