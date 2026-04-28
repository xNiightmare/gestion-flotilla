package com.grandedev.gestionflotilla.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grandedev.gestionflotilla.dto.UsuarioRequestDTO;
import com.grandedev.gestionflotilla.dto.UsuarioResponseDTO;
import com.grandedev.gestionflotilla.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listarUsuarios() {
        return this.usuarioService.traerUsuarios();
    }

    @PostMapping
    public UsuarioResponseDTO crearUsuario(@RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return this.usuarioService.crearUsuario(usuarioRequestDTO);
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO buscarUsuarioPorId(@PathVariable Long id) {
        return this.usuarioService.buscarUsuarioPorId(id);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequestDTO usuarioRequestDTO) {
        return this.usuarioService.actualizarUsuario(id, usuarioRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        this.usuarioService.eliminarUsuario(id);
    }
}
