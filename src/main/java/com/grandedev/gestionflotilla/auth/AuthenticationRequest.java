package com.grandedev.gestionflotilla.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "Nombre de usuario requerido")
    private String username;

    @NotBlank(message = "Contrasenia requerida")
    private String password;
}
