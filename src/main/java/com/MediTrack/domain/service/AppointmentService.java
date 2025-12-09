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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
    

    public AppointmentViewDTO saveDTO(AppointmentDTO dto, String pacienteEmail) {

        if (dto.getFechaCita() == null || dto.getHoraCita() == null) {
            throw new IllegalArgumentException("Fecha y hora son obligatorias");
        }

        if (dto.getMedicoId() == null || dto.getMedicoId().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar un médico");
        }

        if (dto.getEspecialidadId() == null) {
            throw new IllegalArgumentException("Debe seleccionar una especialidad");
        }

        LocalTime hora = dto.getHoraCita();
        if (hora.isBefore(LocalTime.of(7, 0)) || hora.isAfter(LocalTime.of(22, 0))) {
            throw new IllegalArgumentException("La hora de la cita debe estar entre 07:00 y 22:00");
        }

        ZoneId zonaLima = ZoneId.of("America/Lima");
        LocalDateTime citaDateTime = LocalDateTime.of(dto.getFechaCita(), dto.getHoraCita());
        LocalDateTime ahora = LocalDateTime.now(zonaLima);

        if (citaDateTime.isBefore(ahora)) {
            throw new IllegalArgumentException("No puede agendar citas en el pasado");
        }

        if (citaDateTime.isBefore(ahora.plusMinutes(30))) {
            throw new IllegalArgumentException("Debe agendar la cita con al menos 30 minutos de anticipación");
        }

        User paciente = userRepository.findByEmail(pacienteEmail)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        dto.setPacienteId(paciente.getCodigo());

        if (repository.existsByPacienteIdAndFechaCitaAndHoraCita(
                paciente.getCodigo(), dto.getFechaCita(), dto.getHoraCita())) {
            throw new IllegalArgumentException("Ya tienes una cita agendada a esta hora");
        }

        if (repository.existsByMedicoCodigoUsuarioAndFechaCitaAndHoraCita(
                dto.getMedicoId(), dto.getFechaCita(), dto.getHoraCita())) {
            throw new IllegalArgumentException("El médico no está disponible a esta hora");
        }

        MedicProfile medico = medicProfileCrud.findByCodigoUsuario(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        Specialty especialidad = specialtyCrud.findById(dto.getEspecialidadId())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        boolean tieneEspecialidad = medico.getEspecialidades().stream()
                .anyMatch(esp -> esp.getId().equals(dto.getEspecialidadId()));

        if (!tieneEspecialidad) {
            throw new IllegalArgumentException("El médico no pertenece a la especialidad seleccionada");
        }

        Appointment entity = mapper.toAppointment(dto);
        entity.setEstado("PENDIENTE");
        entity.setPaciente(paciente);
        entity.setMedico(medico);
        entity.setEspecialidad(especialidad);

        Appointment saved = repository.save(entity);

        return viewMapper.toViewDTO(saved);
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


    // Obtener todas las citas (para el dashboard)
    public List<AppointmentViewDTO> getAllAppointmentsView() {
        return repository.findAll()
                .stream()
                .map(viewMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    // Nuevos métodos para enviar al frontend con nombres
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

    // Validar que el usuario sea administrador
    public boolean validarAdmin(String email) {
        return userRepository.findByEmail(email)
                .map(user -> "ADMIN".equalsIgnoreCase(user.getRol()))
                .orElse(false);
    }

    // Guardar cita como administrador (sin validar email)
    public AppointmentViewDTO saveDTOAsAdmin(AppointmentDTO dto) {
        if (dto.getPacienteId() == null || dto.getPacienteId().trim().isEmpty()) {
            throw new RuntimeException("El código del paciente es obligatorio");
        }

        if (dto.getMedicoId() == null || dto.getMedicoId().trim().isEmpty()) {
            throw new RuntimeException("El código del médico es obligatorio");
        }

        if (dto.getEspecialidadId() == null || dto.getEspecialidadId() <= 0) {
            throw new RuntimeException("Debe seleccionar una especialidad");
        }

        Appointment entity = mapper.toAppointment(dto);
        entity.setEstado("PENDIENTE");

        MedicProfile medico = medicProfileCrud.findByCodigoUsuario(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        if (medico.getUser().getNombre().matches("\\d+")) {
            throw new RuntimeException("El nombre del médico no puede contener solo números");
        }

        entity.setMedico(medico);

        // Buscar paciente
        User paciente = userRepository.findByCodigo(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        // Validar que el nombre del paciente no sea solo números
        if (paciente.getNombre().matches("\\d+")) {
            throw new RuntimeException("El nombre del paciente no puede contener solo números");
        }

        entity.setPaciente(paciente);

        // Buscar especialidad
        Specialty especialidad = specialtyCrud.findById(dto.getEspecialidadId())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        entity.setEspecialidad(especialidad);

        // Guardar en BD
        Appointment saved = repository.save(entity);

        return viewMapper.toViewDTO(saved);
    }



    public Optional<AppointmentViewDTO> getByIdView(Long id) {
        return repository.findById(id).map(viewMapper::toViewDTO);
    }


    public Optional<AppointmentViewDTO> updateAppointmentView(Long id, AppointmentDTO dto) {
        Optional<Appointment> opt = repository.findById(id);
        if (opt.isEmpty()) return Optional.empty();

        Appointment entity = opt.get();

        if (dto.getMedicoId() != null && !dto.getMedicoId().equals(entity.getMedico().getCodigoUsuario())) {
            MedicProfile medico = medicProfileCrud.findByCodigoUsuario(dto.getMedicoId())
                    .orElseThrow(() -> new RuntimeException("Médico no encontrado"));
            entity.setMedico(medico);
        }

        if (dto.getPacienteId() != null && !dto.getPacienteId().equals(entity.getPaciente().getCodigo())) {
            User paciente = userRepository.findByCodigo(dto.getPacienteId())
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            entity.setPaciente(paciente);
        }

        if (dto.getEspecialidadId() != null) {
            Specialty especialidad = specialtyCrud.findById(dto.getEspecialidadId())
                    .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
            entity.setEspecialidad(especialidad);
        }

        if (dto.getFechaCita() != null) {
            if (dto.getFechaCita().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("La fecha de la cita no puede ser en el pasado");
            }
            entity.setFechaCita(dto.getFechaCita());
        }

        if (dto.getHoraCita() != null) {
            LocalTime hora = dto.getHoraCita();
            if (hora.isBefore(LocalTime.of(7,0)) || hora.isAfter(LocalTime.of(19,0))) {
                throw new IllegalArgumentException("La hora de la cita debe estar entre 07:00 y 19:00");
            }
            entity.setHoraCita(hora);
        }

        if (repository.existsByPacienteIdAndFechaCitaAndHoraCita(entity.getPaciente().getCodigo(), entity.getFechaCita(), entity.getHoraCita())) {
            throw new IllegalArgumentException("El paciente ya tiene otra cita a esta hora");
        }

        if (repository.existsByMedicoCodigoUsuarioAndFechaCitaAndHoraCita(entity.getMedico().getCodigoUsuario(), entity.getFechaCita(), entity.getHoraCita())) {
            throw new IllegalArgumentException("El médico ya tiene otra cita a esta hora");
        }

        if (dto.getEstado() != null) {
            List<String> estadosValidos = List.of("PENDIENTE", "CONFIRMADA", "CANCELADA", "FINALIZADA");
            String estadoUpper = dto.getEstado().toUpperCase();
            if (!estadosValidos.contains(estadoUpper)) {
                throw new IllegalArgumentException("Estado inválido");
            }
            entity.setEstado(estadoUpper);
        }

        Appointment saved = repository.save(entity);
        return Optional.of(viewMapper.toViewDTO(saved));
    }


    // Eliminar cita
    public boolean deleteAppointment(Long id) {
        if (repository.findById(id).isEmpty()) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    // Métodos existentes para DTO normal
    public List<AppointmentDTO> getByPacienteIdDTO(String pacienteId) {
        return repository.findByPacienteId(pacienteId)
                .stream()
                .map(mapper::toAppointmentDTO)
                .collect(Collectors.toList());
    }

    // Ahora recibe String codigoMedico
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