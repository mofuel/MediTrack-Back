package com.MediTrack.domain.dto;

import java.time.LocalTime;

public class ClinicalShiftsDTO {

    private Long id;
    private String nombre;
    private LocalTime horaInicio;
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
