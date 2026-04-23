package com.grandedev.gestionflotilla.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grandedev.gestionflotilla.service.DocumentoService;

import com.grandedev.gestionflotilla.model.Documento;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {
    
    private final DocumentoService documentoService;

    DocumentoController(DocumentoService documentoService){
        this.documentoService = documentoService;
    }

    @PostMapping
    Documento nuevoDocumento(@RequestBody Documento documento){
        return this.documentoService.crearDocumento(documento);
    }

    @GetMapping("/{id}")
    Documento obtenerDocumentoPorId(@PathVariable Long id){
        return this.documentoService.buscarDocumentoPorId(id);
    }

    @PutMapping("/{id}")
    Documento actualizarDocumento(@RequestBody Documento documento, @PathVariable Long id){
        return this.documentoService.actualizarDocumento(documento, id);
    }
    
    @GetMapping //White label error page se quito al cambiar @GetMapping("/") por @GetMapping
    public List<Documento> listarDocumentos(){
        return this.documentoService.listarDocumentos();
    }

    @GetMapping("/camiones/{camionId}")
    public List<Documento> listarDocumenosPorCamion(@PathVariable Long camionId){
        return this.documentoService.listarDocumentoPorCamion(camionId);
    }

    @GetMapping("/operadores/{operadorId}")
    public List<Documento> listarDocumentosPorOperador(@PathVariable Long operadorId){
        return this.documentoService.listarDocumentosPorOperador(operadorId);
    }

    @PutMapping("/{documentoId}/operadores/{operadorId}")
    public Documento asignarDocumentoAOperador(@PathVariable Long documentoId, @PathVariable Long operadorId){
        return this.documentoService.asignarDocumentoAOperador(documentoId, operadorId);
    }

    @PutMapping("/{documentoId}/camiones/{camionId}")
    public Documento asignarDocumentoACamion(@PathVariable Long documentoId, @PathVariable Long camionId){
        return this.documentoService.asignarDocumentoACamion(documentoId, camionId);
    }

    @PostMapping("/operadores/{operadorId}/camiones/{camionId}")
    public Documento registrarSubidaDeDocumento(@RequestBody Documento nuevoDocumento, @PathVariable(required = false) Long operadorId,
                                                    @PathVariable(required = false) Long camionId ){
        return this.documentoService.registrarSubidaDocumento(nuevoDocumento, operadorId, camionId);
    }

    @DeleteMapping("/{documentoId}")
    public void eliminarDocumentoFisicoYLogico(@PathVariable Long documentoId){
        this.documentoService.eliminarDocumentoFisicoYLogico(documentoId);
    }
}
