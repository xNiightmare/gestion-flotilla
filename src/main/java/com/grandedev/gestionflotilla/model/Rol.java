package com.grandedev.gestionflotilla.model;

import static com.grandedev.gestionflotilla.model.Permission.ADMIN_CREATE;
import static com.grandedev.gestionflotilla.model.Permission.ADMIN_DELETE;
import static com.grandedev.gestionflotilla.model.Permission.ADMIN_READ;
import static com.grandedev.gestionflotilla.model.Permission.ADMIN_UPDATE;
import static com.grandedev.gestionflotilla.model.Permission.CAMION_CREATE;
import static com.grandedev.gestionflotilla.model.Permission.CAMION_DELETE;
import static com.grandedev.gestionflotilla.model.Permission.CAMION_READ;
import static com.grandedev.gestionflotilla.model.Permission.CAMION_UPDATE;
import static com.grandedev.gestionflotilla.model.Permission.DOCUMENTO_CREATE;
import static com.grandedev.gestionflotilla.model.Permission.DOCUMENTO_DELETE;
import static com.grandedev.gestionflotilla.model.Permission.DOCUMENTO_READ;
import static com.grandedev.gestionflotilla.model.Permission.DOCUMENTO_UPDATE;
import static com.grandedev.gestionflotilla.model.Permission.OPERADOR_READ;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@RequiredArgsConstructor
public enum Rol {
    ADMIN(
        Set.of(
            ADMIN_READ,
            ADMIN_CREATE,
            ADMIN_UPDATE,
            ADMIN_DELETE,
            OPERADOR_READ,
            CAMION_READ,
            CAMION_CREATE,
            CAMION_UPDATE,
            CAMION_DELETE,
            DOCUMENTO_READ,
            DOCUMENTO_CREATE,
            DOCUMENTO_UPDATE,
            DOCUMENTO_DELETE
        )
    ),
    OPERADOR(
        Set.of(
            OPERADOR_READ,
            DOCUMENTO_READ
        )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
            .stream()
            .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
            .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
