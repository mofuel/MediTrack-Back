package com.MediTrack.persistance.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "especialidad")
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @ManyToMany(mappedBy = "especialidades")
    private Set<MedicProfile> medicos;

    //Getters y Setters

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public Set<MedicProfile> getMedicos() {return medicos;}

    public void setMedicos(Set<MedicProfile> medicos) {this.medicos = medicos;}
}
