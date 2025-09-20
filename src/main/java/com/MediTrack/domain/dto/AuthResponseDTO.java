package com.MediTrack.domain.dto;


/**
 * DTO de respuesta para la autenticación
 */

public class AuthResponseDTO {

    private String token;

    //Constructor
    public AuthResponseDTO(String token) {
        this.token = token;
    }

    // Getter y Setter
    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}
}
