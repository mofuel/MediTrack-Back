package com.MediTrack.persistance.repositoryimpl;

import com.MediTrack.domain.dto.MedicShiftDTO;
import com.MediTrack.domain.repository.MedicShiftRepository;
import com.MediTrack.persistance.crud.MedicShiftCrudRepository;
import com.MediTrack.persistance.entity.MedicShift;
import com.MediTrack.persistance.mapper.MedicShiftMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MedicShiftRepositoryImpl implements MedicShiftRepository {

    @Autowired
    private MedicShiftCrudRepository crud;

    @Autowired
    private MedicShiftMapper mapper;

    @Override
    public MedicShiftDTO save(MedicShiftDTO dto) {
        MedicShift entity = mapper.toMedicShift(dto);
        entity = crud.save(entity);
        return mapper.toMedicShiftDTO(entity);
    }

    @Override
    public Optional<MedicShiftDTO> findById(Long id) {
        return crud.findById(id).map(mapper::toMedicShiftDTO);
    }

    @Override
    public List<MedicShiftDTO> findAll() {
        return crud.findAll().stream()
                .map(mapper::toMedicShiftDTO)
                .toList();
    }

    @Override
    public List<MedicShiftDTO> findByPerfilId(Long perfilId) {
        return crud.findByPerfilMedico_Id(perfilId)
                .stream()
                .map(mapper::toMedicShiftDTO)
                .toList();
    }

    @Override
    public List<MedicShiftDTO> findByTurnoId(Long turnoId) {
        return crud.findByTurno_Id(turnoId)
                .stream()
                .map(mapper::toMedicShiftDTO)
                .toList();
    }

    @Override
    public boolean existsByPerfilAndDia(Long perfilId, short diaSemana) {
        return crud.existsByPerfilMedico_IdAndDiaSemana(perfilId, diaSemana);
    }

    @Override
    public void delete(Long id) {
        crud.deleteById(id);
    }
}
