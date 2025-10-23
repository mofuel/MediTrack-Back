package com.MediTrack.persistance.mapper;

import com.MediTrack.domain.dto.ClinicalShiftsDTO;
import com.MediTrack.persistance.entity.ClinicalShifts;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClinicalShiftsMapper {

    ClinicalShiftsDTO toClinicalShiftDTO(ClinicalShifts entity);

    ClinicalShifts toClinicalShift(ClinicalShiftsDTO dto);
}
