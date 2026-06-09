package com.grandedev.gestionflotilla.resetPassword;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter @Setter
public class ResetPasswordDTO {
    @NotBlank(message = "Contrasenia no puede ser null")
    @Size(min = 8, message = "La contrasenia por lo menos debe tener 8 caracteres")
    private String nuevaPassword;

}
