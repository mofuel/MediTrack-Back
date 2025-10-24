package com.MediTrack.persistance.repositoryimpl;

import com.MediTrack.domain.dto.ClinicalShiftsDTO;
import com.MediTrack.domain.repository.ClinicalShiftsRepository;
import com.MediTrack.persistance.crud.ClinicalShiftsCrudRepository;
import com.MediTrack.persistance.entity.ClinicalShifts;
import com.MediTrack.persistance.mapper.ClinicalShiftsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class ClinicalShiftsRepositoryImpl implements ClinicalShiftsRepository {

    @Autowired
    private ClinicalShiftsCrudRepository crud;

    @Autowired
    private ClinicalShiftsMapper mapper;

    @Override
    public ClinicalShiftsDTO save(ClinicalShiftsDTO dto) {
        ClinicalShifts shift = mapper.toClinicalShift(dto);
        ClinicalShifts saved = crud.save(shift);
        return mapper.toClinicalShiftDTO(saved);
    }

    @Override
    public Optional<ClinicalShiftsDTO> findById(Long id) {
        return crud.findById(id).map(mapper::toClinicalShiftDTO);
    }

    @Override
    public Optional<ClinicalShiftsDTO> findByNombre(String nombre) {
        return crud.findByNombre(nombre).map(mapper::toClinicalShiftDTO);
    }

    @Override
    public List<ClinicalShiftsDTO> findAll() {
        return crud.findAll()
                .stream()
                .map(mapper::toClinicalShiftDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        crud.deleteById(id);
    }
}
