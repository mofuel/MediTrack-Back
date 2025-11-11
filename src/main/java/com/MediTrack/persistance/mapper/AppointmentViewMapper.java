package com.MediTrack.persistance.mapper;

import com.MediTrack.domain.dto.AppointmentDTO;
import com.MediTrack.domain.dto.AppointmentViewDTO;
import com.MediTrack.persistance.entity.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentViewMapper {

    AppointmentDTO toAppointmentDTO(Appointment entity);
    Appointment toAppointment(AppointmentDTO dto);

    // Nuevo metodo para enviar nombres al frontend
    default AppointmentViewDTO toViewDTO(Appointment entity) {
        if (entity == null) return null;

        AppointmentViewDTO dto = new AppointmentViewDTO();
        dto.setId(entity.getId());
        dto.setPacienteNombre(entity.getPaciente().getNombre() + " " + entity.getPaciente().getApellido());

        // corregido:
        dto.setMedicoNombre(entity.getMedico().getUser().getNombre() + " " + entity.getMedico().getUser().getApellido());

        dto.setEspecialidadNombre(entity.getEspecialidad().getNombre());
        dto.setFechaCita(entity.getFechaCita());
        dto.setHoraCita(entity.getHoraCita());
        dto.setEstado(entity.getEstado());
        dto.setFechaCreacion(entity.getFechaCreacion());
        return dto;
    }

}