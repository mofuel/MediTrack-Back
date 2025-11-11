package com.MediTrack.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentDTO {

    private Long id;
    private String pacienteId;
    private String medicoId;
    private Long especialidadId;
    private LocalDate fechaCita;
    private LocalTime horaCita;
    private String estado;
    private LocalDateTime fechaCreacion;

    // Getters y Setters
    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getPacienteId() {return pacienteId;}

    public void setPacienteId(String pacienteId) {this.pacienteId = pacienteId;}

    public String getMedicoId() {return medicoId;}

    public void setMedicoId(String medicoId) {this.medicoId = medicoId;}

    public Long getEspecialidadId() {return especialidadId;}

    public void setEspecialidadId(Long especialidadId) {this.especialidadId = especialidadId;}

    public LocalDate getFechaCita() {return fechaCita;}

    public void setFechaCita(LocalDate fechaCita) {this.fechaCita = fechaCita;}

    public LocalTime getHoraCita() {return horaCita;}

    public void setHoraCita(LocalTime horaCita) {this.horaCita = horaCita;}

    public String getEstado() {return estado;}

    public void setEstado(String estado) {this.estado = estado;}

    public LocalDateTime getFechaCreacion() {return fechaCreacion;}

    public void setFechaCreacion(LocalDateTime fechaCreacion) {this.fechaCreacion = fechaCreacion;}
}
