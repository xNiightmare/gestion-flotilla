package com.grandedev.gestionflotilla.auth;

import com.grandedev.gestionflotilla.model.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Nombre de usuario requerido")
    private String username;

    @NotBlank(message = "Email requerido")
    @Email(message = "Email invalido")
    private String email;

    @NotBlank(message = "Contrasenia requerida")
    @Size(min = 8, message = "La contrasenia debe tener al menos 8 caracteres")
    private String password;

    @NotNull(message = "El rol es requerido")
    private Rol rol;
}
