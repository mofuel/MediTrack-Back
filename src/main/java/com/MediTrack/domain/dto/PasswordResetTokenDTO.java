package com.MediTrack.domain.dto;

import java.time.LocalDateTime;

public class PasswordResetTokenDTO {
    private Long id;
    private String userCodigo;
    private LocalDateTime expiresAt;
    private boolean used;

}
