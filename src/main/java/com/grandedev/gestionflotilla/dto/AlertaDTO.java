package com.grandedev.gestionflotilla.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.grandedev.gestionflotilla.model.TipoAlerta;
import com.grandedev.gestionflotilla.model.TipoDocumento;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlertaDTO {
    private Long id;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaCreacion;
    private boolean leida;
    private TipoAlerta tipo;
    private Long documentoId;
    private TipoDocumento tipoDocumento;
    private String nombreArchivo;
    private LocalDate fechaVencimiento;
}
