package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.ClinicalShiftsDTO;
import com.MediTrack.domain.repository.ClinicalShiftsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicalShiftsService {

    @Autowired
    private ClinicalShiftsRepository repository;

    public ClinicalShiftsDTO save(ClinicalShiftsDTO dto) {
        return repository.save(dto);
    }

    public Optional<ClinicalShiftsDTO> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<ClinicalShiftsDTO> findByNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    public List<ClinicalShiftsDTO> findAll() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
