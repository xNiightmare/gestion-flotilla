package com.grandedev.gestionflotilla.notification;

import com.grandedev.gestionflotilla.model.Documento;
import com.grandedev.gestionflotilla.model.TipoAlerta;
import com.grandedev.gestionflotilla.model.Usuario;

public record NotificationContext(
        Usuario destinatario,
        Documento documento,
        TipoAlerta tipo,
        String titulo,
        String mensaje
) {
}
