package com.MediTrack.persistance.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "turnos_clinica")
public class ClinicalShifts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    //Getters y Setters

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public LocalTime getHoraInicio() {return horaInicio;}

    public void setHoraInicio(LocalTime horaInicio) {this.horaInicio = horaInicio;}

    public LocalTime getHoraFin() {return horaFin;}

    public void setHoraFin(LocalTime horaFin) {this.horaFin = horaFin;}
}
