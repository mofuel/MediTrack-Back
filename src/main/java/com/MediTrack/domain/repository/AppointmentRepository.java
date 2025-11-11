package com.MediTrack.domain.repository;

import com.MediTrack.persistance.entity.Appointment;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {

    Appointment save(Appointment appointment);

    Optional<Appointment> findById(Long id);

    List<Appointment> findByPacienteId(Long pacienteId);

    List<Appointment> findByMedicoId(Long medicoId);

    List<Appointment> findByEstado(String estado);

    void deleteById(Long id);

    long count();
}
