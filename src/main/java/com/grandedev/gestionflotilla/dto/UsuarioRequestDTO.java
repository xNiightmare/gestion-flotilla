package com.grandedev.gestionflotilla.dto;

import com.grandedev.gestionflotilla.model.Rol;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioRequestDTO {
    private String username;
    private String password;
    private Rol rol; //o con string, dependiendo de cómo quieras manejarlo en el DTO
}
