package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.AppointmentDTO;
import com.MediTrack.domain.repository.AppointmentRepository;
import com.MediTrack.persistance.entity.Appointment;
import com.MediTrack.persistance.mapper.AppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository repository;

    @Autowired
    private AppointmentMapper mapper;

    // Guardar cita usando DTO
    public AppointmentDTO saveDTO(AppointmentDTO dto) {
        Appointment entity = mapper.toAppointment(dto);
        entity.setEstado("PENDIENTE"); // siempre pendiente al crear
        Appointment saved = repository.save(entity);
        return mapper.toAppointmentDTO(saved);
    }

    // Listar citas por paciente (DTO)
    public List<AppointmentDTO> getByPacienteIdDTO(String pacienteId) {
        return repository.findByPacienteId(pacienteId)
                .stream()
                .map(mapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    // Listar citas por médico (DTO)
    public List<AppointmentDTO> getByMedicoIdDTO(Long medicoId) {
        return repository.findByMedicoId(medicoId)
                .stream()
                .map(mapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    // Listar por estado (DTO)
    public List<AppointmentDTO> getByEstadoDTO(String estado) {
        return repository.findByEstado(estado)
                .stream()
                .map(mapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    // Cambiar estado de cita (DTO)
    public Optional<AppointmentDTO> updateEstadoDTO(Long id, String nuevoEstado) {
        Optional<Appointment> opt = repository.findById(id);
        opt.ifPresent(a -> {
            a.setEstado(nuevoEstado);
            repository.save(a);
        });
        return opt.map(mapper::toAppointmentDTO);
    }
}