package com.MediTrack.domain.service;

import com.MediTrack.domain.repository.AppointmentRepository;
import com.MediTrack.persistance.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository repository;

    // Crear o actualizar una cita
    public Appointment save(Appointment appointment) {
        return repository.save(appointment);
    }

    // Buscar cita por ID
    public Optional<Appointment> getById(Long id) {
        return repository.findById(id);
    }

    // Buscar todas las citas de un paciente
    public List<Appointment> getByPacienteId(String pacienteId) {
        return repository.findByPacienteId(pacienteId);
    }

    // Buscar todas las citas de un médico
    public List<Appointment> getByMedicoId(Long medicoId) {
        return repository.findByMedicoId(medicoId);
    }

    // Buscar citas por estado
    public List<Appointment> getByEstado(String estado) {
        return repository.findByEstado(estado);
    }

    // Eliminar cita por ID
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    // Contar todas las citas
    public long count() {
        return repository.count();
    }

    // Cambiar estado de la cita (pendiente, aceptada, rechazada)
    public Optional<Appointment> updateEstado(Long id, String nuevoEstado) {
        Optional<Appointment> optionalAppointment = repository.findById(id);
        optionalAppointment.ifPresent(appointment -> {
            appointment.setEstado(nuevoEstado);
            repository.save(appointment);
        });
        return optionalAppointment;
    }
}
