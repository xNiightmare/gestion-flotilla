package com.grandedev.gestionflotilla.login;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDTO {
    @NotBlank(message = "Nombre de usuario requerido")
    private String username;
    @NotBlank(message = "Campo requerido")
    private String password;
}
