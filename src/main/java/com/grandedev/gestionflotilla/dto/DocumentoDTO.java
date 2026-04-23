package com.grandedev.gestionflotilla.dto;

import java.time.LocalDateTime;

import com.grandedev.gestionflotilla.model.TipoDocumento;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DocumentoDTO {
    private Long id;

    private TipoDocumento tipoDocumento;
    private String nombreArchivo;
    private String rutaArchivo;
    private LocalDateTime fechaSubida;
    private Long idOperador;
    private Long idCamion;
}
