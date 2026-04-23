package com.grandedev.gestionflotilla.dto;

import java.time.LocalDate;

import lombok.*;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperadorDTO {

    private Long id;
    private String nombre;
    private String licencia;
    private LocalDate vencimientoLicencia;

    private List<DocumentoDTO> documentos; //Basado en los enums

    private String nss; 
    private String rfc; 
    private String curp; 
}
