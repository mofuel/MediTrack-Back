package com.MediTrack.persistance.crud;

import com.MediTrack.persistance.entity.MedicProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio de acceso a datos para la entidad MedicProfile
 */

public interface MedicProfileCrudRepository extends JpaRepository<MedicProfile, Long> {

    Optional<MedicProfile> findByCodigoUsuario(String codigoUsuario);

    boolean existsByCodigoUsuario(String codigoUsuario);
}
