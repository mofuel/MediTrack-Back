package com.MediTrack.persistance.crud;

import com.MediTrack.persistance.entity.MedicShift;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicShiftCrudRepository extends JpaRepository<MedicShift, Long> {

    // Filtra por el ID del perfil médico (relación ManyToOne)
    List<MedicShift> findByPerfilMedico_Id(Long perfilId);

    // Filtra por el ID del turno (relación ManyToOne)
    List<MedicShift> findByTurno_Id(Long turnoId);

    // Verifica si existe un turno para un médico en un día específico
    boolean existsByPerfilMedico_IdAndDiaSemana(Long perfilId, short diaSemana);
}
