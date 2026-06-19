package com.grandedev.gestionflotilla.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.grandedev.gestionflotilla.exception.ResourceNotFoundException;
import com.grandedev.gestionflotilla.model.Alerta;
import com.grandedev.gestionflotilla.model.Documento;
import com.grandedev.gestionflotilla.model.Operador;
import com.grandedev.gestionflotilla.model.Rol;
import com.grandedev.gestionflotilla.model.TipoAlerta;
import com.grandedev.gestionflotilla.model.Usuario;
import com.grandedev.gestionflotilla.notification.NotificationContext;
import com.grandedev.gestionflotilla.notification.NotificationService;
import com.grandedev.gestionflotilla.repository.AlertaRepository;
import com.grandedev.gestionflotilla.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class AlertaServiceTest {

    @Mock
    private AlertaRepository alertaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private NotificationService notificationService;

    private AlertaService alertaService;

    @BeforeEach
    void setUp() {
        alertaService = new AlertaService(alertaRepository, usuarioRepository, List.of(notificationService));
    }

    @Test
    void notificaAlOperadorYATodosLosAdministradores() {
        Operador operador = Operador.builder().id(10L).nombre("Ana").build();
        Documento documento = Documento.builder().id(20L).operador(operador).build();
        Usuario usuarioOperador = usuario(30L, Rol.OPERADOR);
        Usuario adminUno = usuario(40L, Rol.ADMIN);
        Usuario adminDos = usuario(50L, Rol.ADMIN);

        when(usuarioRepository.findByOperadorId(10L)).thenReturn(Optional.of(usuarioOperador));
        when(usuarioRepository.findAllByRol(Rol.ADMIN)).thenReturn(List.of(adminUno, adminDos));

        alertaService.crearAlertas(documento, TipoAlerta.VENCE_7_DIAS, "Título", "Mensaje");

        ArgumentCaptor<NotificationContext> captor = ArgumentCaptor.forClass(NotificationContext.class);
        verify(notificationService, org.mockito.Mockito.times(3)).notificar(captor.capture());
        assertThat(captor.getAllValues())
                .extracting(context -> context.destinatario().getId())
                .containsExactly(30L, 40L, 50L);
    }

    @Test
    void marcaComoLeidaSoloUnaAlertaPropia() {
        Documento documento = Documento.builder()
                .id(20L)
                .nombreArchivo("licencia.pdf")
                .fechaVencimiento(LocalDate.of(2026, 7, 20))
                .build();
        Alerta alerta = Alerta.builder()
                .id(1L)
                .titulo("Documento próximo a vencer")
                .mensaje("Mensaje")
                .fechaCreacion(LocalDateTime.now())
                .tipo(TipoAlerta.VENCE_30_DIAS)
                .documento(documento)
                .usuario(usuario(30L, Rol.OPERADOR))
                .leida(false)
                .build();
        when(alertaRepository.findByIdAndUsuarioId(1L, 30L)).thenReturn(Optional.of(alerta));

        assertThat(alertaService.marcarLeida(1L, 30L).isLeida()).isTrue();
        assertThat(alerta.isLeida()).isTrue();
    }

    @Test
    void rechazaUnaAlertaAjena() {
        when(alertaRepository.findByIdAndUsuarioId(1L, 99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> alertaService.marcarLeida(1L, 99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private Usuario usuario(Long id, Rol rol) {
        return Usuario.builder().id(id).username("usuario-" + id).rol(rol).build();
    }
}
