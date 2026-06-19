package com.grandedev.gestionflotilla.notification;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grandedev.gestionflotilla.model.Alerta;
import com.grandedev.gestionflotilla.repository.AlertaRepository;

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SystemNotificationService implements NotificationService {

    private final AlertaRepository alertaRepository;
    private final Clock clock;

    public SystemNotificationService(AlertaRepository alertaRepository, Clock clock) {
        this.alertaRepository = alertaRepository;
        this.clock = clock;
    }

    @Override
    @Transactional
    public void notificar(NotificationContext context) {
        boolean existe = alertaRepository.existsByUsuarioIdAndDocumentoIdAndTipo(
                context.destinatario().getId(),
                context.documento().getId(),
                context.tipo()
        );

        if (existe) {
            return;
        }

        alertaRepository.save(Alerta.builder()
                .titulo(context.titulo())
                .mensaje(context.mensaje())
                .fechaCreacion(LocalDateTime.now(clock))
                .leida(false)
                .tipo(context.tipo())
                .usuario(context.destinatario())
                .documento(context.documento())
                .build());
    }
}
