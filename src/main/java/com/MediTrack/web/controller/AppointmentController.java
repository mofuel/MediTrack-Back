package com.MediTrack.web.controller;

import com.MediTrack.domain.dto.AppointmentDTO;
import com.MediTrack.domain.dto.AppointmentViewDTO;
import com.MediTrack.domain.service.AppointmentService;
import com.MediTrack.persistance.entity.Appointment;
import com.MediTrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO dto,
                                                            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.getUsernameFromToken(token);

        // Ignorar dto.pacienteId enviado por frontend
        AppointmentDTO saved = appointmentService.saveDTO(dto, email);
        return ResponseEntity.ok(saved);
    }


    // --- DEVOLVER nombres para la tabla del frontend ---
    @GetMapping("/paciente/{codigoPaciente}")
    public ResponseEntity<List<AppointmentViewDTO>> getByPaciente(
            @PathVariable String codigoPaciente,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.getUsernameFromToken(token);

        boolean autorizado = appointmentService.validarPaciente(email, codigoPaciente);
        if (!autorizado) {
            return ResponseEntity.status(403).build();
        }

        List<AppointmentViewDTO> citas = appointmentService.getByPacienteIdView(codigoPaciente);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<AppointmentViewDTO>> getByMedico(@PathVariable Long medicoId) {
        List<AppointmentViewDTO> citas = appointmentService.getByMedicoIdView(medicoId);
        return ResponseEntity.ok(citas);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<AppointmentViewDTO> updateEstado(
            @PathVariable Long id,
            @RequestParam String estado
    ) {
        Optional<AppointmentViewDTO> updated = appointmentService.updateEstadoView(id, estado.toUpperCase());
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<AppointmentViewDTO>> getPendientes() {
        List<AppointmentViewDTO> pendientes = appointmentService.getByEstadoView("PENDIENTE");
        return ResponseEntity.ok(pendientes);
    }
}
