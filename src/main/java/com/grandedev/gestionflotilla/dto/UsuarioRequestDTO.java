package com.grandedev.gestionflotilla.dto;

import com.grandedev.gestionflotilla.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioRequestDTO {
    @NotBlank(message = "Nombre de usuario requerido")
    private String username;

    @NotBlank(message = "Contrasenia no puede ser null")
    @Size(min = 8, message = "La contrasenia por lo menos debe tener 8 caracteres")
    private String password;

    @NotNull(message = "El ROL es necesario")
    private Rol rol; //o con string, dependiendo de cómo quieras manejarlo en el DTO
}
