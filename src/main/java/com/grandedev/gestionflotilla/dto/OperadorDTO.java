package com.grandedev.gestionflotilla.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperadorDTO {

    private Long id;
    @NotBlank(message = "Nombre requerido")
    private String nombre;
    @NotBlank(message = "Licencia requerida")
    private String licencia;
    @NotNull
    private LocalDate vencimientoLicencia;

    private List<DocumentoDTO> documentos; //Basado en los enums

    private String nss; 
    private String rfc; 
    private String curp; 
}
