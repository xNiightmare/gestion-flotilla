package com.grandedev.gestionflotilla.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grandedev.gestionflotilla.model.Documento;
import com.grandedev.gestionflotilla.model.TipoAlerta;
import com.grandedev.gestionflotilla.repository.DocumentoRepository;

@ExtendWith(MockitoExtension.class)
class VencimientoServiceTest {

    private static final LocalDate HOY = LocalDate.of(2026, 6, 19);
    private static final Clock CLOCK = Clock.fixed(
            Instant.parse("2026-06-19T14:00:00Z"),
            ZoneId.of("America/Mexico_City")
    );

    @Mock
    private DocumentoRepository documentoRepository;

    @Mock
    private AlertaService alertaService;

    @ParameterizedTest
    @CsvSource({
            "30,VENCE_30_DIAS",
            "7,VENCE_7_DIAS",
            "1,VENCE_1_DIA",
            "0,VENCIDA",
            "-10,VENCIDA"
    })
    void generaAlertaEnCadaUmbral(long dias, TipoAlerta tipoEsperado) {
        Documento documento = documento(100L, HOY.plusDays(dias));
        when(documentoRepository.findByFechaVencimientoIsNotNullAndFechaVencimientoLessThanEqual(HOY.plusDays(30)))
                .thenReturn(List.of(documento));

        VencimientoService service = new VencimientoService(documentoRepository, alertaService, CLOCK);

        assertThat(service.evaluarVencimientos()).isEqualTo(1);
        verify(alertaService).crearAlertas(eq(documento), eq(tipoEsperado), anyString(), anyString());
    }

    @Test
    void ignoraDocumentoFueraDeLosUmbrales() {
        Documento documento = documento(101L, HOY.plusDays(12));
        when(documentoRepository.findByFechaVencimientoIsNotNullAndFechaVencimientoLessThanEqual(HOY.plusDays(30)))
                .thenReturn(List.of(documento));

        VencimientoService service = new VencimientoService(documentoRepository, alertaService, CLOCK);

        assertThat(service.evaluarVencimientos()).isZero();
        verify(alertaService, never()).crearAlertas(eq(documento), eq(TipoAlerta.VENCE_7_DIAS), anyString(), anyString());
    }

    private Documento documento(Long id, LocalDate fechaVencimiento) {
        return Documento.builder()
                .id(id)
                .nombreArchivo("poliza.pdf")
                .fechaVencimiento(fechaVencimiento)
                .build();
    }
}
