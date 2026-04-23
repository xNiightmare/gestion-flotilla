package com.grandedev.gestionflotilla.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CamionDTO {
    private Long id;
    private Long idOperador;
    private String marca;
    private String motor;
    private int anio;
    private String serie;
    
    private List<DocumentoDTO> documentos; //Tipo de documento que se le asigna al camion, puede ser un complemento o factura, dependiendo del caso 
    
    private LocalDate inicioVigencia;
    private LocalDate finVigencia;
    private String formaDePago;
    private String rutaArchivo; //Complemento o factura
    private LocalDate fechaDePago;
    private LocalDate vencimientoPago;
}
