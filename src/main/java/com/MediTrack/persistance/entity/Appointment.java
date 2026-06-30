package com.MediTrack.persistance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "cita", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"medico_id", "fecha_cita", "hora_cita"})
})
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private User paciente;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private MedicProfile medico;

    @ManyToOne
    @JoinColumn(name = "especialidad_id", nullable = false)
    private Specialty especialidad;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDate fechaCita;

    @Column(name = "hora_cita", nullable = false)
    private LocalTime horaCita;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos = 60;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus estado = AppointmentStatus.PENDIENTE;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

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

    public LocalTime getHoraFin() {return horaFin;}

    public void setHoraFin(LocalTime horaFin) {this.horaFin = horaFin;}

    public Integer getDuracionMinutos() {return duracionMinutos;}

    public void setDuracionMinutos(Integer duracionMinutos) {this.duracionMinutos = duracionMinutos;}

    public AppointmentStatus getEstado() {return estado;}

    public void setEstado(AppointmentStatus estado) {this.estado = estado;}

    public LocalDateTime getFechaCreacion() {return fechaCreacion;}

    public void setFechaCreacion(LocalDateTime fechaCreacion) {this.fechaCreacion = fechaCreacion;}
}