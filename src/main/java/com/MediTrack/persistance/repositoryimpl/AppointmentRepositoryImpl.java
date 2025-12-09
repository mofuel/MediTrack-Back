package com.MediTrack.persistance.repositoryimpl;

import com.MediTrack.domain.repository.AppointmentRepository;
import com.MediTrack.persistance.crud.AppointmentCrudRepository;
import com.MediTrack.persistance.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentRepositoryImpl implements AppointmentRepository {

    @Autowired
    private AppointmentCrudRepository crud;

    @Override
    public Appointment save(Appointment appointment) {
        return crud.save(appointment);
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return crud.findById(id);
    }

    @Override
    public List<Appointment> findByPacienteId(String codigoPaciente) {
        return crud.findByPaciente_Codigo(codigoPaciente);
    }

    @Override
    public List<Appointment> findByMedicoId(Long medicoId) {
        return crud.findByMedico_Id(medicoId);
    }

    @Override
    public List<Appointment> findByEstado(String estado) {
        return crud.findByEstado(estado);
    }

    @Override
    public List<Appointment> findByMedicoCodigoUsuario(String codigoUsuario) {
        return crud.findByMedico_CodigoUsuario(codigoUsuario);
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>();
        crud.findAll().forEach(appointments::add);
        return appointments;
    }

    @Override
    public void deleteById(Long id) {
        crud.deleteById(id);
    }

    @Override
    public long count() {
        return crud.count();
    }

    @Override
    public boolean existsByPacienteIdAndFechaCitaAndHoraCita(
            String pacienteId,
            LocalDate fecha,
            LocalTime hora
    ) {
        return crud.existsByPaciente_CodigoAndFechaCitaAndHoraCita(pacienteId, fecha, hora);
    }

    @Override
    public boolean existsByMedicoCodigoUsuarioAndFechaCitaAndHoraCita(
            String codigoMedico,
            LocalDate fecha,
            LocalTime hora
    ) {
        return crud.existsByMedico_CodigoUsuarioAndFechaCitaAndHoraCita(codigoMedico, fecha, hora);
    }
}
