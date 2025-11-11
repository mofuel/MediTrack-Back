package com.MediTrack.persistance.crud;

import com.MediTrack.persistance.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentCrudRepository extends JpaRepository<Appointment, Long> {

    // Buscar todas las citas de un paciente
    List<Appointment> findByPacienteId(Long pacienteId);

    // Buscar todas las citas de un médico
    List<Appointment> findByMedicoId(Long medicoId);

    // Buscar por estado (PENDIENTE, ACEPTADA, RECHAZADA)
    List<Appointment> findByEstado(String estado);
}
