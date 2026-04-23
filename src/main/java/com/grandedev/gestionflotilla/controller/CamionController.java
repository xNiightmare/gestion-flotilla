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

import com.grandedev.gestionflotilla.model.Camion;
import com.grandedev.gestionflotilla.model.Operador;
import com.grandedev.gestionflotilla.service.CamionService;
import com.grandedev.gestionflotilla.service.OperadorService;

@RestController 
@RequestMapping("/camiones")
public class CamionController {
    private final CamionService camionService;
    private final OperadorService operadorService;

    CamionController(CamionService camionService, OperadorService operadorService){
        this.camionService = camionService;
        this.operadorService = operadorService;
        
    }

    @GetMapping //White label error page se quito al cambiar @GetMapping("/") por @GetMapping
    List<Camion> obtenerCamiones(){
        return this.camionService.listarCamiones();
    }
    
    @PostMapping
    Camion nuevoCamion(@RequestBody Camion nuevoCamion){
        return this.camionService.crearCamion(nuevoCamion);
    }

    @GetMapping("/{id}")
    Camion obtenerCamion(@PathVariable Long id){
        return this.camionService.buscarCamionPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarCamionPorId(@PathVariable Long id){
        camionService.eliminarCamionPorId(id);
    }
      
    @PutMapping("/{id}")
    public Camion actualizarCamion(@RequestBody Camion camionActualizado ,@PathVariable Long id){
        return this.camionService.actualizarCamion(camionActualizado, id);
    }

    //Asignamos a un camion un operador. Nota: Para asignar algo se usa Put, Post NO!
    @PutMapping("/{camionId}/operadores/{operadorId}")
    Camion asignarOperador(@PathVariable Long camionId, @PathVariable Long operadorId){
        Camion camion = this.camionService.buscarCamionPorId(camionId);
        Operador operador = this.operadorService.buscarOperadorPorId(operadorId);

        camion.setOperador(operador);
        camionService.crearCamion(camion); //No usar @Transactional, porque al hacer esto, Spring Boot ya maneja la transacción
        return camion;
    }
}
