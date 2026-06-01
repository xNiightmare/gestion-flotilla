package com.grandedev.gestionflotilla.login;


import ch.qos.logback.core.subst.Token;
import com.grandedev.gestionflotilla.model.Rol;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
    private Token token;
    private String username;
    private Rol rol;
}