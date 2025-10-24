package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.MedicProfileDTO;
import com.MediTrack.domain.repository.MedicProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MedicProfileService {

    @Autowired
    private MedicProfileRepository repository;

    public MedicProfileDTO save(MedicProfileDTO dto) {
        return repository.save(dto);
    }

    public Optional<MedicProfileDTO> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<MedicProfileDTO> findByCodigoUsuario(String codigoUsuario) {
        return repository.findByCodigoUsuario(codigoUsuario);
    }

    public List<MedicProfileDTO> findAll() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
