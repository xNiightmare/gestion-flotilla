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
    ResponseEntity<List<CamionDTO>> obtenerCamiones(){ return ResponseEntity.ok(this.camionService.listarCamiones());}

    @PostMapping
    public ResponseEntity<CamionDTO> nuevoCamion(
            @Valid @RequestBody CamionDTO camionDTO)
    {return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.camionService.crearCamion(camionDTO));}

    @GetMapping("/{id}")
    public ResponseEntity<CamionDTO> buscarCamionPorId(@PathVariable Long id){
        return ResponseEntity.ok(this.camionService.buscarCamionPorId(id));
    }

    @PutMapping("/{camionId}/operadores/{operadorId}")
    public ResponseEntity<CamionDTO> asignarOperador(@PathVariable Long camionId, @PathVariable Long operadorId){
        return ResponseEntity.ok(this.camionService.asignarOperador(camionId, operadorId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CamionDTO> actualizarCamion(@Valid @RequestBody CamionDTO camionActualizado, @PathVariable Long id){
        return ResponseEntity.ok(this.camionService.actualizarCamion(camionActualizado,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCamionPorId(@PathVariable Long id){
        this.camionService.eliminarCamionPorId(id);
        return ResponseEntity.noContent().build();
    }

}
