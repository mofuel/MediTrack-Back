package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.AppointmentDTO;
import com.MediTrack.domain.dto.AppointmentViewDTO;
import com.MediTrack.domain.dto.SlotDisponibleDTO;
import com.MediTrack.domain.repository.AppointmentRepository;
import com.MediTrack.domain.repository.SpecialtyRepository;
import com.MediTrack.domain.repository.UserRepository;
import com.MediTrack.persistance.crud.MedicProfileCrudRepository;
import com.MediTrack.persistance.crud.SpecialtyCrudRepository;
import com.MediTrack.persistance.entity.*;
import com.MediTrack.persistance.mapper.AppointmentMapper;
import com.MediTrack.persistance.mapper.AppointmentViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private DisponibilidadService disponibilidadService;

    @Transactional
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

        ZoneId zonaLima = ZoneId.of("America/Lima");
        LocalDateTime citaDateTime = LocalDateTime.of(dto.getFechaCita(), dto.getHoraCita());
        LocalDateTime ahora = LocalDateTime.now(zonaLima);

        if (citaDateTime.isBefore(ahora)) {
            throw new IllegalArgumentException("No puede agendar citas en el pasado");
        }

        if (citaDateTime.isBefore(ahora.plusMinutes(30))) {
            throw new IllegalArgumentException("Debe agendar la cita con al menos 30 minutos de anticipación");
        }

        List<SlotDisponibleDTO> slots = disponibilidadService.obtenerSlotsDisponibles(
                dto.getMedicoId(), dto.getFechaCita());

        boolean slotValido = slots.stream()
                .anyMatch(s -> s.getHoraInicio().equals(dto.getHoraCita()) && s.isDisponible());

        if (!slotValido) {
            throw new IllegalArgumentException("El horario seleccionado no está disponible");
        }

        User paciente = userRepository.findByEmail(pacienteEmail)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        dto.setPacienteId(paciente.getCodigo());

        MedicProfile medico = medicProfileCrud.findByCodigoUsuario(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        Specialty especialidad = specialtyCrud.findById(dto.getEspecialidadId())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        boolean tieneEspecialidad = medico.getEspecialidades().stream()
                .anyMatch(esp -> esp.getId().equals(dto.getEspecialidadId()));

        if (!tieneEspecialidad) {
            throw new IllegalArgumentException("El médico no pertenece a la especialidad seleccionada");
        }

        if (repository.existsByPacienteIdAndFechaCitaAndHoraCita(
                paciente.getCodigo(), dto.getFechaCita(), dto.getHoraCita())) {
            throw new IllegalArgumentException("Ya tienes una cita agendada a esta hora");
        }

        if (repository.existsByMedicoCodigoUsuarioAndFechaCitaAndHoraCita(
                dto.getMedicoId(), dto.getFechaCita(), dto.getHoraCita())) {
            throw new IllegalArgumentException("El horario ya fue reservado por otro paciente");
        }

        Appointment entity = mapper.toAppointment(dto);
        entity.setEstado(AppointmentStatus.PENDIENTE);
        entity.setPaciente(paciente);
        entity.setMedico(medico);
        entity.setEspecialidad(especialidad);
        entity.setDuracionMinutos(60);
        entity.setHoraFin(dto.getHoraCita().plusMinutes(60));

        Appointment saved = repository.save(entity);

        return viewMapper.toViewDTO(saved);
    }

    @Transactional
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

        if (dto.getFechaCita() == null || dto.getHoraCita() == null) {
            throw new IllegalArgumentException("Fecha y hora son obligatorias");
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

        List<SlotDisponibleDTO> slots = disponibilidadService.obtenerSlotsDisponibles(
                dto.getMedicoId(), dto.getFechaCita());

        boolean slotValido = slots.stream()
                .anyMatch(s -> s.getHoraInicio().equals(dto.getHoraCita()) && s.isDisponible());

        if (!slotValido) {
            throw new IllegalArgumentException("El horario seleccionado no está disponible");
        }

        if (repository.existsByMedicoCodigoUsuarioAndFechaCitaAndHoraCita(
                dto.getMedicoId(), dto.getFechaCita(), dto.getHoraCita())) {
            throw new IllegalArgumentException("El horario seleccionado ya no está disponible");
        }




        Appointment entity = mapper.toAppointment(dto);
        entity.setEstado(AppointmentStatus.PENDIENTE);

        MedicProfile medico = medicProfileCrud.findByCodigoUsuario(dto.getMedicoId())
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        entity.setMedico(medico);

        User paciente = userRepository.findByCodigo(dto.getPacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (repository.existsByPacienteIdAndFechaCitaAndHoraCita(
                paciente.getCodigo(), dto.getFechaCita(), dto.getHoraCita())) {
            throw new IllegalArgumentException("El paciente ya tiene una cita agendada a esta hora");
        }

        entity.setPaciente(paciente);

        Specialty especialidad = specialtyCrud.findById(dto.getEspecialidadId())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        entity.setEspecialidad(especialidad);
        entity.setDuracionMinutos(60);
        entity.setHoraFin(dto.getHoraCita().plusMinutes(60));

        Appointment saved = repository.save(entity);

        return viewMapper.toViewDTO(saved);
    }

    public boolean validarPaciente(String emailPaciente, String codigoPaciente) {
        return userRepository.findByEmail(emailPaciente)
                .map(user -> user.getCodigo().equals(codigoPaciente))
                .orElse(false);
    }

    public boolean validarMedico(String emailMedico, String codigoMedico) {
        return medicProfileCrud.findByCodigoUsuario(codigoMedico)
                .map(medico -> medico.getUser() != null &&
                        medico.getUser().getEmail().equals(emailMedico))
                .orElse(false);
    }

    public List<AppointmentViewDTO> getAllAppointmentsView() {
        return repository.findAll()
                .stream()
                .map(viewMapper::toViewDTO)
                .collect(Collectors.toList());
    }

    public List<AppointmentViewDTO> getByPacienteIdView(String pacienteId) {
        return repository.findByPacienteId(pacienteId)
                .stream()
                .map(viewMapper::toViewDTO)
                .collect(Collectors.toList());
    }

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

    @Transactional
    public Optional<AppointmentViewDTO> updateEstadoView(Long id, String nuevoEstado) {
        Optional<Appointment> opt = repository.findById(id);
        opt.ifPresent(a -> {
            try {
                a.setEstado(AppointmentStatus.valueOf(nuevoEstado.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado inválido: " + nuevoEstado);
            }
            repository.save(a);
        });
        return opt.map(viewMapper::toViewDTO);
    }

    public boolean validarAdmin(String email) {
        return userRepository.findByEmail(email)
                .map(user -> "ADMIN".equalsIgnoreCase(user.getRol()))
                .orElse(false);
    }

    @Transactional
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

        if (dto.getFechaCita() != null || dto.getHoraCita() != null) {
            LocalDate fechaValidar = (dto.getFechaCita() != null) ? dto.getFechaCita() : entity.getFechaCita();
            LocalTime horaValidar = (dto.getHoraCita() != null) ? dto.getHoraCita() : entity.getHoraCita();

            List<SlotDisponibleDTO> slots = disponibilidadService.obtenerSlotsDisponibles(
                    entity.getMedico().getCodigoUsuario(), fechaValidar, entity.getId());

            boolean slotValido = slots.stream()
                    .anyMatch(s -> s.getHoraInicio().equals(horaValidar) && s.isDisponible());

            if (!slotValido) {
                throw new IllegalArgumentException("El horario seleccionado no está disponible");
            }

            if (dto.getFechaCita() != null) {
                entity.setFechaCita(dto.getFechaCita());
            }
            if (dto.getHoraCita() != null) {
                entity.setHoraCita(dto.getHoraCita());
                entity.setHoraFin(dto.getHoraCita().plusMinutes(60));
            }
        }

        if (dto.getEstado() != null) {
            try {
                entity.setEstado(AppointmentStatus.valueOf(dto.getEstado().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado inválido");
            }
        }

        Appointment saved = repository.save(entity);
        return Optional.of(viewMapper.toViewDTO(saved));
    }

    public Optional<AppointmentViewDTO> getByIdView(Long id) {
        return repository.findById(id).map(viewMapper::toViewDTO);
    }

    public boolean deleteAppointment(Long id) {
        if (repository.findById(id).isEmpty()) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

    public List<SlotDisponibleDTO> obtenerSlotsDisponibles(String medicoId, LocalDate fecha) {
        return disponibilidadService.obtenerSlotsDisponibles(medicoId, fecha);
    }
}