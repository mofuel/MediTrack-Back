package com.MediTrack.persistance.mapper;


import com.MediTrack.domain.dto.MedicShiftDTO;
import com.MediTrack.persistance.entity.MedicShift;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicShiftMapper {

    @Mapping(source = "perfilMedico.id",               target = "perfilId")
    @Mapping(source = "turno.id",                      target = "turnoId")
    @Mapping(source = "turno.nombre",                  target = "nombreTurno")
    @Mapping(source = "perfilMedico.user.nombre",      target = "nombreMedico")
    MedicShiftDTO toMedicShiftDTO(MedicShift entity);

    @Mapping(target = "perfilMedico.id", source = "perfilId")
    @Mapping(target = "turno.id",        source = "turnoId")
    MedicShift toMedicShift(MedicShiftDTO dto);
}
