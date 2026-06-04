package com.grandedev.gestionflotilla.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:create"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),
    OPERADOR_READ("operador:read"),
    OPERADOR_CREATE("operador:create"),
    OPERADOR_UPDATE("operador:update"),
    OPERADOR_DELETE("operador:delete"),
    CAMION_READ("camion:read"),
    CAMION_CREATE("camion:create"),
    CAMION_UPDATE("camion:update"),
    CAMION_DELETE("camion:delete"),
    DOCUMENTO_READ("documento:read"),
    DOCUMENTO_CREATE("documento:create"),
    DOCUMENTO_UPDATE("documento:update"),
    DOCUMENTO_DELETE("documento:delete");

    @Getter
    private final String permission;
}
