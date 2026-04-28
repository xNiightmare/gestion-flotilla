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

import com.grandedev.gestionflotilla.dto.CamionDTO;
import com.grandedev.gestionflotilla.service.CamionService;

@RestController
@RequestMapping("/camiones")
public class CamionController {
    private final CamionService camionService;

    CamionController(CamionService camionService) {
        this.camionService = camionService;
    }

    @GetMapping
    List<CamionDTO> obtenerCamiones() {
        return this.camionService.listarCamiones();
    }

    @PostMapping
    CamionDTO nuevoCamion(@RequestBody CamionDTO nuevoCamion) {
        return this.camionService.crearCamion(nuevoCamion);
    }

    @GetMapping("/{id}")
    CamionDTO obtenerCamion(@PathVariable Long id) {
        return this.camionService.buscarCamionPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarCamionPorId(@PathVariable Long id) {
        camionService.eliminarCamionPorId(id);
    }

    @PutMapping("/{id}")
    public CamionDTO actualizarCamion(@RequestBody CamionDTO camionActualizado, @PathVariable Long id) {
        return this.camionService.actualizarCamion(camionActualizado, id);
    }

    @PutMapping("/{camionId}/operadores/{operadorId}")
    CamionDTO asignarOperador(@PathVariable Long camionId, @PathVariable Long operadorId) {
        return this.camionService.asignarOperador(camionId, operadorId);
    }
}
