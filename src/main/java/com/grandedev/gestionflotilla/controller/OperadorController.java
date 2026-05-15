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

import com.grandedev.gestionflotilla.dto.OperadorDTO;
import com.grandedev.gestionflotilla.service.OperadorService;

@RestController
@RequestMapping("/operadores")
public class OperadorController {
    private final OperadorService operadorService;

    public OperadorController(OperadorService operadorService) {
        this.operadorService = operadorService;
    }

    @GetMapping
    ResponseEntity<List<OperadorDTO>> obtenerOperadores(){return ResponseEntity.ok(this.operadorService.listarOperadores());}

    @PostMapping
    public ResponseEntity<OperadorDTO> crearOperador(
            @Valid @RequestBody OperadorDTO operadorDTO
    ){return ResponseEntity.status(HttpStatus.CREATED)
            .body(this.operadorService.crearOperador(operadorDTO));}


    @GetMapping("/{id}")
    public ResponseEntity<OperadorDTO> buscarOperadorPorId(@PathVariable Long id){
        return ResponseEntity.ok(this.operadorService.buscarOperadorPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OperadorDTO> actualizarOperador(@PathVariable Long id, @Valid @RequestBody OperadorDTO operadorDTO){
        return ResponseEntity.ok(this.operadorService.actualizarOperador(id,operadorDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOperadorPorId(@PathVariable Long id){
        this.operadorService.eliminarOperadorPorId(id);
        return ResponseEntity.noContent().build();
    }
}
