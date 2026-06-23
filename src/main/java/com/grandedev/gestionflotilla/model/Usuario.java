package com.grandedev.gestionflotilla.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @OneToOne
    @JoinColumn(name="operador_id")
    private Operador operador;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Column(name = "telegram_chat_id")
    private Long telegramChatId;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rol.getAuthorities();
    }
}
