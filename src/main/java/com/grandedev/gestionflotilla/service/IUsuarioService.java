package com.grandedev.gestionflotilla.service;

import java.util.List;

import com.grandedev.gestionflotilla.dto.UsuarioRequestDTO;
import com.grandedev.gestionflotilla.dto.UsuarioResponseDTO;


public interface IUsuarioService {

    List<UsuarioResponseDTO> traerUsuarios();
    UsuarioResponseDTO buscarUsuarioPorId(Long id);
    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuarioRequestDTO);
    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO usuarioRequestDTO);
    void eliminarUsuario(Long id);
}
