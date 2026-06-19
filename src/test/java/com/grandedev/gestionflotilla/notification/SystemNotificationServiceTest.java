package com.grandedev.gestionflotilla.notification;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grandedev.gestionflotilla.model.Alerta;
import com.grandedev.gestionflotilla.model.Documento;
import com.grandedev.gestionflotilla.model.TipoAlerta;
import com.grandedev.gestionflotilla.model.Usuario;
import com.grandedev.gestionflotilla.repository.AlertaRepository;

@ExtendWith(MockitoExtension.class)
class SystemNotificationServiceTest {

    @Mock
    private AlertaRepository alertaRepository;

    private SystemNotificationService service;
    private NotificationContext context;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2026-06-19T14:00:00Z"), ZoneId.of("America/Mexico_City"));
        service = new SystemNotificationService(alertaRepository, clock);
        Usuario usuario = Usuario.builder().id(1L).build();
        Documento documento = Documento.builder().id(2L).build();
        context = new NotificationContext(
                usuario,
                documento,
                TipoAlerta.VENCE_7_DIAS,
                "Documento próximo a vencer",
                "La póliza vence en 7 días."
        );
    }

    @Test
    void persisteUnaAlertaNueva() {
        when(alertaRepository.existsByUsuarioIdAndDocumentoIdAndTipo(1L, 2L, TipoAlerta.VENCE_7_DIAS))
                .thenReturn(false);

        service.notificar(context);

        verify(alertaRepository).save(any(Alerta.class));
    }

    @Test
    void noDuplicaUnaAlertaExistente() {
        when(alertaRepository.existsByUsuarioIdAndDocumentoIdAndTipo(1L, 2L, TipoAlerta.VENCE_7_DIAS))
                .thenReturn(true);

        service.notificar(context);

        verify(alertaRepository, never()).save(any(Alerta.class));
    }
}
