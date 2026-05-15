package com.grandedev.gestionflotilla.service;

import java.util.List;

import com.grandedev.gestionflotilla.exception.ResourceNotFoundException;
import com.grandedev.gestionflotilla.exception.UsernameAlreadyExistsException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.grandedev.gestionflotilla.dto.UsuarioRequestDTO;
import com.grandedev.gestionflotilla.dto.UsuarioResponseDTO;
import com.grandedev.gestionflotilla.mapper.Mapper;
import com.grandedev.gestionflotilla.model.Usuario;
import com.grandedev.gestionflotilla.repository.UsuarioRepository;

@Service
public class UsuarioService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UsuarioResponseDTO> traerUsuarios() {
        return usuarioRepository.findAll().stream().map(Mapper::toUsuarioResponseDTO).toList();
    }

    @Override
    public UsuarioResponseDTO buscarUsuarioPorId(Long id) {
        return Mapper.toUsuarioResponseDTO(this.buscarUsuarioEntidadPorId(id));
    }

    @Override
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        if(this.usuarioRepository.findByUsername(usuarioRequestDTO.getUsername()).isPresent())
            throw new UsernameAlreadyExistsException("Nombre de usuario NO disponible. Intenta con otro.");
        //Keep it simple, saassssale de volada

        Usuario usuario = Mapper.toUsuario(usuarioRequestDTO);
        usuario.setPassword(passwordEncoder.encode(usuarioRequestDTO.getPassword()));

        return Mapper.toUsuarioResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = this.buscarUsuarioEntidadPorId(id);
        usuario.setUsername(usuarioRequestDTO.getUsername());
        usuario.setRol(usuarioRequestDTO.getRol());

        if (usuarioRequestDTO.getPassword() != null && !usuarioRequestDTO.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(usuarioRequestDTO.getPassword()));
        }

        return Mapper.toUsuarioResponseDTO(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminarUsuario(Long id) {
        this.buscarUsuarioEntidadPorId(id);
        this.usuarioRepository.deleteById(id);
    }

    private Usuario buscarUsuarioEntidadPorId(Long id) {
        return this.usuarioRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("404 Error: User not found with ID: " + id));
    }


}
