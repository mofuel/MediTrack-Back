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

    // ✅ NUEVO: Validar que el médico solo vea sus propias citas
    public boolean validarMedico(String emailMedico, String codigoMedico) {
        return medicProfileCrud.findByCodigoUsuario(codigoMedico)
                .map(medico -> {
                    // Verificar que el email del user asociado al medico coincida
                    return medico.getUser() != null &&
                            medico.getUser().getEmail().equals(emailMedico);
                })
                .orElse(false);
    }


    // Obtener todas las citas (para el dashboard)
    public List<AppointmentViewDTO> getAllAppointmentsView() {
        return repository.findAll()
                .stream()
                .map(viewMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    // --- Nuevos métodos para enviar al frontend con nombres ---
    public List<AppointmentViewDTO> getByPacienteIdView(String pacienteId) {
        return repository.findByPacienteId(pacienteId)
                .stream()
                .map(viewMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    //  Ahora recibe String codigoMedico en lugar de Long medicoId
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

    public Optional<AppointmentViewDTO> updateEstadoView(Long id, String nuevoEstado) {
        Optional<Appointment> opt = repository.findById(id);
        opt.ifPresent(a -> {
            a.setEstado(nuevoEstado);
            repository.save(a);
        });
        return opt.map(viewMapper::toViewDTO);
    }

    // ✅ NUEVO: Validar que el usuario sea administrador
    public boolean validarAdmin(String email) {
        return userRepository.findByEmail(email)
                .map(user -> "ADMIN".equalsIgnoreCase(user.getRol()))
                .orElse(false);
    }

    // ✅ NUEVO: Guardar cita como administrador (sin validar email)
    public AppointmentViewDTO saveDTOAsAdmin(AppointmentDTO dto) {
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
        return viewMapper.toViewDTO(saved);
    }

    // ✅ NUEVO: Obtener cita por ID
    public Optional<AppointmentViewDTO> getByIdView(Long id) {
        return repository.findById(id).map(viewMapper::toViewDTO);
    }

    // ✅ NUEVO: Actualizar cita completa
    public Optional<AppointmentViewDTO> updateAppointmentView(Long id, AppointmentDTO dto) {
        Optional<Appointment> opt = repository.findById(id);
        if (opt.isEmpty()) {
            return Optional.empty();
        }

        Appointment entity = opt.get();

        // Actualizar médico si cambió
        if (dto.getMedicoId() != null && !dto.getMedicoId().equals(entity.getMedico().getCodigoUsuario())) {
            MedicProfile medico = medicProfileCrud.findByCodigoUsuario(dto.getMedicoId())
                    .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
            entity.setMedico(medico);
        }

        // Actualizar paciente si cambió
        if (dto.getPacienteId() != null && !dto.getPacienteId().equals(entity.getPaciente().getCodigo())) {
            User paciente = userRepository.findByCodigo(dto.getPacienteId())
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            entity.setPaciente(paciente);
        }

        // Actualizar especialidad si cambió
        if (dto.getEspecialidadId() != null) {
            Specialty especialidad = specialtyCrud.findById(dto.getEspecialidadId())
                    .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
            entity.setEspecialidad(especialidad);
        }

        // Actualizar fecha, hora y estado
        if (dto.getFechaCita() != null) entity.setFechaCita(dto.getFechaCita());
        if (dto.getHoraCita() != null) entity.setHoraCita(dto.getHoraCita());
        if (dto.getEstado() != null) entity.setEstado(dto.getEstado().toUpperCase());

        Appointment saved = repository.save(entity);
        return Optional.of(viewMapper.toViewDTO(saved));
    }

    // ✅ NUEVO: Eliminar cita
    public boolean deleteAppointment(Long id) {
        if (repository.findById(id).isEmpty()) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    // --- Métodos existentes para DTO normal ---
    public List<AppointmentDTO> getByPacienteIdDTO(String pacienteId) {
        return repository.findByPacienteId(pacienteId)
                .stream()
                .map(mapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    // ✅ CAMBIADO: Ahora recibe String codigoMedico
    public List<AppointmentDTO> getByMedicoIdDTO(String codigoMedico) {
        return repository.findByMedicoCodigoUsuario(codigoMedico)
                .stream()
                .map(mapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getByEstadoDTO(String estado) {
        return repository.findByEstado(estado)
                .stream()
                .map(mapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    public Optional<AppointmentDTO> updateEstadoDTO(Long id, String nuevoEstado) {
        Optional<Appointment> opt = repository.findById(id);
        opt.ifPresent(a -> {
            a.setEstado(nuevoEstado);
            repository.save(a);
        });
        return opt.map(mapper::toAppointmentDTO);
    }
}