package com.MediTrack.persistance.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;       // ID interno

    private String codigo; // ID visible

    private String nombre;

    private String apellido;

    private String dni;

    private String sexo;

    private String email;

    private String telefono;

    private String rol;

    private boolean activo = true;

    private String password;


    //Getters y Setters

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getCodigo() {return codigo;}

    public void setCodigo(String codigo) {this.codigo = codigo;}

    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getApellido() {return apellido;}

    public void setApellido(String apellido) {this.apellido = apellido;}

    public String getDni() {return dni;}

    public void setDni(String dni) {this.dni = dni;}

    public String getSexo() {return sexo;}

    public void setSexo(String sexo) {this.sexo = sexo;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getTelefono() {return telefono;}

    public void setTelefono(String telefono) {this.telefono = telefono;}

    public String getRol() {return rol;}

    public void setRol(String rol) {this.rol = rol;}

    public boolean isActivo() {return activo;}

    public void setActivo(boolean activo) {this.activo = activo;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}
}
