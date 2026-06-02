package com.grandedev.gestionflotilla.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.grandedev.gestionflotilla.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>
{
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
}
//Primero hay que definir una interfaz repositorio en el dominio.
//Esto controla las operaciones que tienen sentido para el problema que se resuelve.