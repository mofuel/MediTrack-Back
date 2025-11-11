package com.MediTrack.web.controller;

import com.MediTrack.domain.dto.AppointmentDTO;
import com.MediTrack.domain.service.AppointmentService;
import com.MediTrack.persistance.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO dto) {
        AppointmentDTO saved = appointmentService.saveDTO(dto);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<AppointmentDTO>> getByPaciente(@PathVariable String pacienteId) {
        List<AppointmentDTO> citas = appointmentService.getByPacienteIdDTO(pacienteId);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<AppointmentDTO>> getByMedico(@PathVariable Long medicoId) {
        List<AppointmentDTO> citas = appointmentService.getByMedicoIdDTO(medicoId);
        return ResponseEntity.ok(citas);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<AppointmentDTO> updateEstado(
            @PathVariable Long id,
            @RequestParam String estado
    ) {
        Optional<AppointmentDTO> updated = appointmentService.updateEstadoDTO(id, estado.toUpperCase());
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<AppointmentDTO>> getPendientes() {
        List<AppointmentDTO> pendientes = appointmentService.getByEstadoDTO("PENDIENTE");
        return ResponseEntity.ok(pendientes);
    }
}