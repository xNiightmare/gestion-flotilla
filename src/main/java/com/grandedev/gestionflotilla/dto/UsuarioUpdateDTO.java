package com.grandedev.gestionflotilla.dto;

import com.grandedev.gestionflotilla.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioUpdateDTO {
    @NotBlank(message = "Nombre de usuario requerido")
    private String username;

    private Long idOperador;

    @NotNull(message = "El ROL es necesario")
    private Rol rol; //o con string, dependiendo de cómo quieras manejarlo en el DTO

}
