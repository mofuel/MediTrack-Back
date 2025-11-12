package com.MediTrack.domain.repository;

import com.MediTrack.persistance.entity.Appointment;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository {

    Appointment save(Appointment appointment);

    Optional<Appointment> findById(Long id);

    List<Appointment> findByPacienteId(String pacienteId);

    List<Appointment> findByMedicoId(Long medicoId);

    List<Appointment> findByMedicoCodigoUsuario(String codigoUsuario);

    List<Appointment> findByEstado(String estado);

    List<Appointment> findAll();

    void deleteById(Long id);

    long count();
}
