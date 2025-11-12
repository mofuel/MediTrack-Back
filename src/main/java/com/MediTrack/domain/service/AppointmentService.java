package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.AppointmentDTO;
import com.MediTrack.domain.dto.AppointmentViewDTO;
import com.MediTrack.domain.repository.AppointmentRepository;
import com.MediTrack.domain.repository.SpecialtyRepository;
import com.MediTrack.domain.repository.UserRepository;
import com.MediTrack.persistance.crud.MedicProfileCrudRepository;
import com.MediTrack.persistance.crud.SpecialtyCrudRepository;
import com.MediTrack.persistance.entity.Appointment;
import com.MediTrack.persistance.entity.MedicProfile;
import com.MediTrack.persistance.entity.Specialty;
import com.MediTrack.persistance.entity.User;
import com.MediTrack.persistance.mapper.AppointmentMapper;
import com.MediTrack.persistance.mapper.AppointmentViewMapper;
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

    @Autowired
    private AppointmentViewMapper viewMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MedicProfileCrudRepository medicProfileCrud;

    @Autowired
    private SpecialtyRepository especialidadRepository;

    @Autowired
    private SpecialtyCrudRepository specialtyCrud;

    public AppointmentDTO saveDTO(AppointmentDTO dto, String pacienteEmail) {
        Appointment entity = mapper.toAppointment(dto);
        entity.setEstado("PENDIENTE");

        // traer medico
        MedicProfile medico = medicProfileCrud.findByCodigoUsuario(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
        entity.setMedico(medico);

        // traer paciente
        User paciente = userRepository.findByCodigo(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        entity.setPaciente(paciente);

        // traer especialidad
        Specialty especialidad = specialtyCrud.findById(dto.getEspecialidadId())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        entity.setEspecialidad(especialidad);

        Appointment saved = repository.save(entity);
        return mapper.toAppointmentDTO(saved);
    }

    public boolean validarPaciente(String emailPaciente, String codigoPaciente) {
        return userRepository.findByEmail(emailPaciente)
                .map(user -> user.getCodigo().equals(codigoPaciente))
                .orElse(false);
    }

    // Validar que el médico solo vea sus propias citas
    public boolean validarMedico(String emailMedico, String codigoMedico) {
        return medicProfileCrud.findByCodigoUsuario(codigoMedico)
                .map(medico -> {
                    // Verificar que el email del user asociado al medico coincida
                    return medico.getUser() != null &&
                            medico.getUser().getEmail().equals(emailMedico);
                })
                .orElse(false);
    }

    // Obtener paciente a traves del id
    public List<AppointmentViewDTO> getByPacienteIdView(String pacienteId) {
        return repository.findByPacienteId(pacienteId)
                .stream()
                .map(viewMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    // Recibe String codigoMedico en lugar de Long medicoId
    public List<AppointmentViewDTO> getByMedicoIdView(String codigoMedico) {
        return repository.findByMedicoCodigoUsuario(codigoMedico)
                .stream()
                .map(viewMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentViewDTO> getByEstadoView(String estado) {
        return repository.findByEstado(estado)
                .stream()
                .map(viewMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentViewDTO> getAllAppointmentsView() {
        return repository.findAll()
                .stream()
                .map(viewMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    public Optional<AppointmentViewDTO> updateEstadoView(Long id, String nuevoEstado) {
        Optional<Appointment> opt = repository.findById(id);
        opt.ifPresent(a -> {
            a.setEstado(nuevoEstado);
            repository.save(a);
        });
        return opt.map(viewMapper::toViewDTO);
    }

}