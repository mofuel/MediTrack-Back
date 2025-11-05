package com.MediTrack.web.controller;

import com.MediTrack.domain.dto.SpecialtyDTO;
import com.MediTrack.domain.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {

    @Autowired
    private SpecialtyService specialtyService;

    // ✅ Crear nueva especialidad (solo ADMIN)
    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createSpecialty(@RequestBody SpecialtyDTO dto) {
        try {
            SpecialtyDTO saved = specialtyService.save(dto);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ Listar todas las especialidades
    @GetMapping
    public ResponseEntity<List<SpecialtyDTO>> getAll() {
        return ResponseEntity.ok(specialtyService.getAll());
    }

    // ✅ Buscar especialidad por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return specialtyService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Buscar especialidad por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getByNombre(@PathVariable String nombre) {
        return specialtyService.getByNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Eliminar especialidad por ID (solo ADMIN)
    // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean deleted = specialtyService.delete(id);
        if (deleted) {
            return ResponseEntity.ok("✅ Especialidad eliminada correctamente");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
