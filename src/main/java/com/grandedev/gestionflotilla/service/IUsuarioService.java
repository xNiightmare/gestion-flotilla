package com.grandedev.gestionflotilla.service;

import java.util.List;

import com.grandedev.gestionflotilla.dto.UsuarioRequestDTO;
import com.grandedev.gestionflotilla.dto.UsuarioResponseDTO;
import com.grandedev.gestionflotilla.resetPassword.ResetPasswordDTO;


public interface IUsuarioService {

    List<UsuarioResponseDTO> traerUsuarios();
    UsuarioResponseDTO buscarUsuarioPorId(Long id);
    UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuarioRequestDTO);
    UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO usuarioRequestDTO);
    UsuarioResponseDTO resetearContrasenia(Long id, ResetPasswordDTO resetPasswordDTO);
    void eliminarUsuario(Long id);
}
