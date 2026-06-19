package com.grandedev.gestionflotilla.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.grandedev.gestionflotilla.model.Documento;
import com.grandedev.gestionflotilla.model.TipoAlerta;
import com.grandedev.gestionflotilla.repository.DocumentoRepository;

@Service
public class VencimientoService {

    private static final Logger log = LoggerFactory.getLogger(VencimientoService.class);

    private final DocumentoRepository documentoRepository;
    private final AlertaService alertaService;
    private final Clock clock;

    public VencimientoService(DocumentoRepository documentoRepository, AlertaService alertaService, Clock clock) {
        this.documentoRepository = documentoRepository;
        this.alertaService = alertaService;
        this.clock = clock;
    }

    public int evaluarVencimientos() {
        LocalDate hoy = LocalDate.now(clock);
        int procesados = 0;

        for (Documento documento : documentoRepository
                .findByFechaVencimientoIsNotNullAndFechaVencimientoLessThanEqual(hoy.plusDays(30))) {
            try {
                Optional<TipoAlerta> tipo = determinarTipo(hoy, documento.getFechaVencimiento());
                if (tipo.isEmpty()) {
                    continue;
                }

                alertaService.crearAlertas(
                        documento,
                        tipo.get(),
                        construirTitulo(tipo.get()),
                        construirMensaje(documento, tipo.get())
                );
                procesados++;
            } catch (RuntimeException ex) {
                log.error("No se pudo evaluar el documento {}", documento.getId(), ex);
            }
        }

        return procesados;
    }

    Optional<TipoAlerta> determinarTipo(LocalDate hoy, LocalDate vencimiento) {
        long dias = ChronoUnit.DAYS.between(hoy, vencimiento);
        if (dias <= 0) return Optional.of(TipoAlerta.VENCIDA);
        if (dias == 1) return Optional.of(TipoAlerta.VENCE_1_DIA);
        if (dias == 7) return Optional.of(TipoAlerta.VENCE_7_DIAS);
        if (dias == 30) return Optional.of(TipoAlerta.VENCE_30_DIAS);
        return Optional.empty();
    }

    private String construirTitulo(TipoAlerta tipo) {
        return tipo == TipoAlerta.VENCIDA ? "Documento vencido" : "Documento próximo a vencer";
    }

    private String construirMensaje(Documento documento, TipoAlerta tipo) {
        String nombre = documento.getNombreArchivo() != null ? documento.getNombreArchivo() : "Documento " + documento.getId();
        if (tipo == TipoAlerta.VENCIDA) {
            return nombre + " venció el " + documento.getFechaVencimiento() + ".";
        }

        long dias = switch (tipo) {
            case VENCE_30_DIAS -> 30;
            case VENCE_7_DIAS -> 7;
            case VENCE_1_DIA -> 1;
            case VENCIDA -> 0;
        };
        return nombre + " vence en " + dias + (dias == 1 ? " día" : " días")
                + ", el " + documento.getFechaVencimiento() + ".";
    }
}
