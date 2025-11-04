package com.MediTrack.web.controller;

import com.MediTrack.domain.dto.MedicProfileDTO;
import com.MediTrack.domain.service.MedicProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfil-medico")
public class MedicProfileController {
    @Autowired
    private MedicProfileService medicProfileService;

    // Crear perfil médico
    @PostMapping("/create")
    public ResponseEntity<MedicProfileDTO> createProfile(@RequestBody MedicProfileDTO dto) {
        MedicProfileDTO saved = medicProfileService.save(dto);
        return ResponseEntity.ok(saved);
    }

    // Obtener perfil médico por ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicProfileDTO> getById(@PathVariable Long id) {
        return medicProfileService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener perfil por código de usuario (FK del usuario)
    @GetMapping("/usuario/{codigoUsuario}")
    public ResponseEntity<MedicProfileDTO> getByCodigoUsuario(@PathVariable String codigoUsuario) {
        return medicProfileService.findByCodigoUsuario(codigoUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar todos los perfiles médicos
    @GetMapping
    public ResponseEntity<List<MedicProfileDTO>> getAll() {
        List<MedicProfileDTO> profiles = medicProfileService.findAll();
        return ResponseEntity.ok(profiles);
    }

    // Eliminar perfil por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicProfileService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
