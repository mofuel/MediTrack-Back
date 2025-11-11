package com.MediTrack.persistance.mapper;

import com.MediTrack.domain.dto.AppointmentDTO;
import com.MediTrack.persistance.entity.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    AppointmentDTO toAppointmentDTO(Appointment entity);

    Appointment toAppointment(AppointmentDTO dto);
}
