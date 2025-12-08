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

    // Crear nueva cita
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO dto,
                                                            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.getUsernameFromToken(token);

        // Ignorar dto.pacienteId enviado por frontend
        AppointmentDTO saved = appointmentService.saveDTO(dto, email);
        return ResponseEntity.ok(saved);
    }

    // Crear cita como admin (sin validar email del paciente)
    @PostMapping("/admin")
    public ResponseEntity<AppointmentViewDTO> createAppointmentAsAdmin(
            @RequestBody AppointmentDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.getUsernameFromToken(token);

        // Validar que sea admin
        boolean esAdmin = appointmentService.validarAdmin(email);
        if (!esAdmin) {
            return ResponseEntity.status(403).build();
        }

        AppointmentViewDTO saved = appointmentService.saveDTOAsAdmin(dto);
        return ResponseEntity.ok(saved);
    }


    // Devolver todas las citas
    @GetMapping("/all")
    public ResponseEntity<List<AppointmentViewDTO>> getAllAppointments(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.getUsernameFromToken(token);

        List<AppointmentViewDTO> todasLasCitas = appointmentService.getAllAppointmentsView();
        return ResponseEntity.ok(todasLasCitas);
    }

    //  Obtener una cita por ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentViewDTO> getAppointmentById(@PathVariable Long id) {
        Optional<AppointmentViewDTO> cita = appointmentService.getByIdView(id);
        return cita.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Devolver nombres para la tabla del frontend
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

    @GetMapping("/medico/{codigoMedico}")
    public ResponseEntity<List<AppointmentViewDTO>> getByMedico(
            @PathVariable String codigoMedico,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.getUsernameFromToken(token);

        // Validar que el médico solo vea sus propias citas
        boolean autorizado = appointmentService.validarMedico(email, codigoMedico);
        if (!autorizado) {
            return ResponseEntity.status(403).build();
        }

        List<AppointmentViewDTO> citas = appointmentService.getByMedicoIdView(codigoMedico);
        return ResponseEntity.ok(citas);
    }

    //  Actualizar cita completa (admin)
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentViewDTO> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.getUsernameFromToken(token);

        // Validar que sea admin
        boolean esAdmin = appointmentService.validarAdmin(email);
        if (!esAdmin) {
            return ResponseEntity.status(403).build();
        }

        Optional<AppointmentViewDTO> updated = appointmentService.updateAppointmentView(id, dto);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Actualizar el estado
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


    //  Eliminar cita (admin)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String email = jwtUtil.getUsernameFromToken(token);

        // Validar que sea admin
        boolean esAdmin = appointmentService.validarAdmin(email);
        if (!esAdmin) {
            return ResponseEntity.status(403).build();
        }

        boolean deleted = appointmentService.deleteAppointment(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
