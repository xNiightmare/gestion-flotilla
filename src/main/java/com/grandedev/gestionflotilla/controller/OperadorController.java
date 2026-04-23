package com.grandedev.gestionflotilla.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grandedev.gestionflotilla.model.Operador;
import com.grandedev.gestionflotilla.service.OperadorService;

@RestController
@RequestMapping("/operadores")
public class OperadorController {
    private final OperadorService operadorService;

    public OperadorController(OperadorService operadorService){
        this.operadorService = operadorService;
    }


    @GetMapping //White label error page se quito al cambiar @GetMapping("/") por @GetMapping
    List<Operador> obtenerOperadores(){
        return this.operadorService.listarOperadores();
    }

    @PostMapping
    Operador nuevoOperador(@RequestBody Operador operador){
        return this.operadorService.crearOperador(operador);
    }

    @GetMapping("/{id}")
    Operador obtenerOperadorPorId(@PathVariable Long id){
        return this.operadorService.buscarOperadorPorId(id);
    }

    @DeleteMapping("/{operadorId}")
    public void eliminarOperadorPorId(@PathVariable Long operadorId){
        this.operadorService
            .buscarOperadorPorId(operadorId);
        
        this.operadorService.eliminarOperadorPorId(operadorId);
    }
}
