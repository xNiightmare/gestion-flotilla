package com.grandedev.gestionflotilla.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.grandedev.gestionflotilla.dto.AlertaCountDTO;
import com.grandedev.gestionflotilla.dto.AlertaDTO;
import com.grandedev.gestionflotilla.dto.AlertasActualizadasDTO;
import com.grandedev.gestionflotilla.model.Usuario;
import com.grandedev.gestionflotilla.service.AlertaService;

@RestController
@RequestMapping("/api/v1/alertas")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @GetMapping
    public ResponseEntity<List<AlertaDTO>> listar(
            @AuthenticationPrincipal Usuario usuario,
            @RequestParam(defaultValue = "false") boolean soloNoLeidas
    ) {
        return ResponseEntity.ok(alertaService.listarPropias(usuario.getId(), soloNoLeidas));
    }

    @GetMapping("/no-leidas/count")
    public ResponseEntity<AlertaCountDTO> contarNoLeidas(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(new AlertaCountDTO(alertaService.contarNoLeidas(usuario.getId())));
    }

    @PatchMapping("/{id}/leer")
    public ResponseEntity<AlertaDTO> marcarLeida(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario
    ) {
        return ResponseEntity.ok(alertaService.marcarLeida(id, usuario.getId()));
    }

    @PatchMapping("/leer-todas")
    public ResponseEntity<AlertasActualizadasDTO> marcarTodasLeidas(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(new AlertasActualizadasDTO(alertaService.marcarTodasLeidas(usuario.getId())));
    }
}
