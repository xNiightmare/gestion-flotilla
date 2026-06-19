package com.grandedev.gestionflotilla.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.grandedev.gestionflotilla.dto.AlertaDTO;
import com.grandedev.gestionflotilla.exception.ResourceNotFoundException;
import com.grandedev.gestionflotilla.model.Alerta;
import com.grandedev.gestionflotilla.model.Documento;
import com.grandedev.gestionflotilla.model.Rol;
import com.grandedev.gestionflotilla.model.TipoAlerta;
import com.grandedev.gestionflotilla.model.Usuario;
import com.grandedev.gestionflotilla.notification.NotificationContext;
import com.grandedev.gestionflotilla.notification.NotificationService;
import com.grandedev.gestionflotilla.repository.AlertaRepository;
import com.grandedev.gestionflotilla.repository.UsuarioRepository;

@Service
public class AlertaService {

    private static final Logger log = LoggerFactory.getLogger(AlertaService.class);

    private final AlertaRepository alertaRepository;
    private final UsuarioRepository usuarioRepository;
    private final List<NotificationService> notificationServices;

    public AlertaService(
            AlertaRepository alertaRepository,
            UsuarioRepository usuarioRepository,
            List<NotificationService> notificationServices
    ) {
        this.alertaRepository = alertaRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificationServices = notificationServices;
    }

    public void crearAlertas(Documento documento, TipoAlerta tipo, String titulo, String mensaje) {
        for (Usuario destinatario : resolverDestinatarios(documento)) {
            NotificationContext context = new NotificationContext(destinatario, documento, tipo, titulo, mensaje);
            for (NotificationService notificationService : notificationServices) {
                try {
                    notificationService.notificar(context);
                } catch (RuntimeException ex) {
                    log.error(
                            "Fallo el canal {} para usuario {} y documento {}",
                            notificationService.getClass().getSimpleName(),
                            destinatario.getId(),
                            documento.getId(),
                            ex
                    );
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public List<AlertaDTO> listarPropias(Long usuarioId, boolean soloNoLeidas) {
        List<Alerta> alertas = soloNoLeidas
                ? alertaRepository.findByUsuarioIdAndLeidaFalseOrderByFechaCreacionDesc(usuarioId)
                : alertaRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
        return alertas.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public long contarNoLeidas(Long usuarioId) {
        return alertaRepository.countByUsuarioIdAndLeidaFalse(usuarioId);
    }

    @Transactional
    public AlertaDTO marcarLeida(Long alertaId, Long usuarioId) {
        Alerta alerta = alertaRepository.findByIdAndUsuarioId(alertaId, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta no encontrada"));
        if (!alerta.isLeida()) {
            alerta.setLeida(true);
        }
        return toDTO(alerta);
    }

    @Transactional
    public int marcarTodasLeidas(Long usuarioId) {
        List<Alerta> pendientes = alertaRepository.findByUsuarioIdAndLeidaFalse(usuarioId);
        pendientes.forEach(alerta -> alerta.setLeida(true));
        return pendientes.size();
    }

    private List<Usuario> resolverDestinatarios(Documento documento) {
        Map<Long, Usuario> destinatarios = new LinkedHashMap<>();

        if (documento.getOperador() != null) {
            usuarioRepository.findByOperadorId(documento.getOperador().getId())
                    .ifPresent(usuario -> destinatarios.put(usuario.getId(), usuario));
        }

        usuarioRepository.findAllByRol(Rol.ADMIN)
                .forEach(usuario -> destinatarios.put(usuario.getId(), usuario));

        return List.copyOf(destinatarios.values());
    }

    private AlertaDTO toDTO(Alerta alerta) {
        Documento documento = alerta.getDocumento();
        return AlertaDTO.builder()
                .id(alerta.getId())
                .titulo(alerta.getTitulo())
                .mensaje(alerta.getMensaje())
                .fechaCreacion(alerta.getFechaCreacion())
                .leida(alerta.isLeida())
                .tipo(alerta.getTipo())
                .documentoId(documento.getId())
                .tipoDocumento(documento.getTipoDocumento())
                .nombreArchivo(documento.getNombreArchivo())
                .fechaVencimiento(documento.getFechaVencimiento())
                .build();
    }
}
