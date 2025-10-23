package com.MediTrack.domain.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class MedicProfileDTO {

    private Long id;
    private String codigoUsuario;
    private LocalDateTime fechaCreacion;
    private String nombreCompleto; // opcional: tomado de User
    private Set<SpecialtyDTO> especialidades;

    //Getters y Setters

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getCodigoUsuario() {return codigoUsuario;}

    public void setCodigoUsuario(String codigoUsuario) {this.codigoUsuario = codigoUsuario;}

    public LocalDateTime getFechaCreacion() {return fechaCreacion;}

    public void setFechaCreacion(LocalDateTime fechaCreacion) {this.fechaCreacion = fechaCreacion;}

    public String getNombreCompleto() {return nombreCompleto;}

    public void setNombreCompleto(String nombreCompleto) {this.nombreCompleto = nombreCompleto;}

    public Set<SpecialtyDTO> getEspecialidades() {return especialidades;}

    public void setEspecialidades(Set<SpecialtyDTO> especialidades) {this.especialidades = especialidades;}
}
