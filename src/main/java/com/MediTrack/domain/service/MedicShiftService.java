package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.MedicShiftDTO;
import com.MediTrack.domain.repository.MedicShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MedicShiftService {

    @Autowired
    private MedicShiftRepository repository;

    public MedicShiftDTO save(MedicShiftDTO dto) {
        return repository.save(dto);
    }

    public Optional<MedicShiftDTO> findById(Long id) {
        return repository.findById(id);
    }

    public List<MedicShiftDTO> findAll() {
        return repository.findAll();
    }

    public List<MedicShiftDTO> findByPerfilId(Long perfilId) {
        return repository.findByPerfilId(perfilId);
    }

    public List<MedicShiftDTO> findByTurnoId(Long turnoId) {
        return repository.findByTurnoId(turnoId);
    }

    public boolean existsByPerfilAndDia(Long perfilId, short diaSemana) {
        return repository.existsByPerfilAndDia(perfilId, diaSemana);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}