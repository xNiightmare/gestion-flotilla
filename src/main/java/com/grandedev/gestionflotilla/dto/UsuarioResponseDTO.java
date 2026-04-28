package com.grandedev.gestionflotilla.dto;

import com.grandedev.gestionflotilla.model.Rol;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private Rol rol; //o con string, dependiendo de cómo quieras manejarlo en el DTO
}
