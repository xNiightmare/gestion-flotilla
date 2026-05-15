package com.grandedev.gestionflotilla.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios(){return ResponseEntity.ok(this.usuarioService.traerUsuarios());}

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crearUsuario(
            @Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.crearUsuario(usuarioRequestDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable Long id){
        return ResponseEntity.ok(this.usuarioService.buscarUsuarioPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO usuarioRequestDTO){
        return ResponseEntity.ok(this.usuarioService.actualizarUsuario(id, usuarioRequestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id){
        this.usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
