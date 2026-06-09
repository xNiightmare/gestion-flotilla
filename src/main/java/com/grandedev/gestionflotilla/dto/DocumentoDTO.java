package com.grandedev.gestionflotilla.dto;

import java.time.Instant;
import java.time.LocalDateTime;

import com.grandedev.gestionflotilla.model.TipoDocumento;

import lombok.*;
import org.jspecify.annotations.Nullable;

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
    private Long tamanioArchivo;
    private String mimeType;
    private Long idOperador;
    private Long idCamion;


}
