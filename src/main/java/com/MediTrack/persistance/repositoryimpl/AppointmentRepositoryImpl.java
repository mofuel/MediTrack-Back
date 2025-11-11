package com.MediTrack.persistance.repositoryimpl;

import com.MediTrack.domain.repository.AppointmentRepository;
import com.MediTrack.persistance.crud.AppointmentCrudRepository;
import com.MediTrack.persistance.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
    public void deleteById(Long id) {
        crud.deleteById(id);
    }

    @Override
    public long count() {
        return crud.count();
    }
}
