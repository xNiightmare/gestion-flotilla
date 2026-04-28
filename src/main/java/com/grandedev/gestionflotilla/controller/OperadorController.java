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
    List<OperadorDTO> obtenerOperadores() {
        return this.operadorService.listarOperadores();
    }

    @PostMapping
    OperadorDTO nuevoOperador(@RequestBody OperadorDTO operador) {
        return this.operadorService.crearOperador(operador);
    }

    @GetMapping("/{id}")
    OperadorDTO obtenerOperadorPorId(@PathVariable Long id) {
        return this.operadorService.buscarOperadorPorId(id);
    }

    @PutMapping("/{id}")
    OperadorDTO actualizarOperador(@PathVariable Long id, @RequestBody OperadorDTO operador) {
        return this.operadorService.actualizarOperador(id, operador);
    }

    @DeleteMapping("/{operadorId}")
    public void eliminarOperadorPorId(@PathVariable Long operadorId) {
        this.operadorService.eliminarOperadorPorId(operadorId);
    }
}
