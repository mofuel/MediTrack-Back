package com.MediTrack.persistance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "cita")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con usuario (paciente)
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private User paciente;

    // Relación con perfil médico
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private MedicProfile medico;

    // Relación con especialidad
    @ManyToOne
    @JoinColumn(name = "especialidad_id", nullable = false)
    private Specialty especialidad;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDate fechaCita;

    @Column(name = "hora_cita", nullable = false)
    private LocalTime horaCita;

    @Column(name = "estado", nullable = false)
    private String estado = "PENDIENTE"; // PENDIENTE, ACEPTADA, RECHAZADA

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Getters y Setters


    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public User getPaciente() {return paciente;}

    public void setPaciente(User paciente) {this.paciente = paciente;}

    public MedicProfile getMedico() {return medico;}

    public void setMedico(MedicProfile medico) {this.medico = medico;}

    public Specialty getEspecialidad() {return especialidad;}

    public void setEspecialidad(Specialty especialidad) {this.especialidad = especialidad;}

    public LocalDate getFechaCita() {return fechaCita;}

    public void setFechaCita(LocalDate fechaCita) {this.fechaCita = fechaCita;}

    public LocalTime getHoraCita() {return horaCita;}

    public void setHoraCita(LocalTime horaCita) {this.horaCita = horaCita;}

    public String getEstado() {return estado;}

    public void setEstado(String estado) {this.estado = estado;}

    public LocalDateTime getFechaCreacion() {return fechaCreacion;}

    public void setFechaCreacion(LocalDateTime fechaCreacion) {this.fechaCreacion = fechaCreacion;}
}
