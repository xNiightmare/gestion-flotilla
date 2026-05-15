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

import com.grandedev.gestionflotilla.dto.DocumentoDTO;
import com.grandedev.gestionflotilla.service.DocumentoService;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;

    DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @GetMapping
    ResponseEntity<List<DocumentoDTO>> listarDocumentos(){return ResponseEntity.ok(this.documentoService.listarDocumentos());}

    @PostMapping
    public ResponseEntity<DocumentoDTO> nuevoDocumento(
            @Valid @RequestBody DocumentoDTO documentoDTO
    ){return  ResponseEntity.status(HttpStatus.CREATED)
            .body(this.documentoService.crearDocumento(documentoDTO));}

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoDTO> obtenerDocumentoPorId(@PathVariable Long id){
        return ResponseEntity.ok(this.documentoService.buscarDocumentoPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentoDTO> actualizarDocumento(@Valid @RequestBody DocumentoDTO documentoDTO,@PathVariable Long id){
        return ResponseEntity.ok(this.documentoService.actualizarDocumento(documentoDTO,id));
    }

    @GetMapping("/camiones/{camionId}")
    public ResponseEntity<List<DocumentoDTO>> listarDocumentoPorCamion(@PathVariable Long camionId){
        return ResponseEntity.ok(this.documentoService.listarDocumentoPorCamion(camionId));
    }

    @GetMapping("/operadores/{operadorId}")
    public ResponseEntity<List<DocumentoDTO>> listarDocumentoPorOperador(@PathVariable Long operadorId){
        return ResponseEntity.ok(this.documentoService.listarDocumentosPorOperador(operadorId));
    }

    @PutMapping("/{documentoId}/operadores/{operadorId}")
    public ResponseEntity<DocumentoDTO> asignarDocumentoAOperador(@PathVariable Long documentoId, @PathVariable Long operadorId){
        return ResponseEntity.ok(this.documentoService.asignarDocumentoAOperador(documentoId, operadorId));
    }

    @PutMapping("/{documentoId}/camiones/{camionId}")
    public ResponseEntity<DocumentoDTO> asignarDocumentoACamion(@PathVariable Long documentoId, @PathVariable Long camionId){
        return ResponseEntity.ok(this.documentoService.asignarDocumentoACamion(documentoId, camionId));
    }

    @PostMapping("/operadores/{operadorId}/camiones/{camionId}")
    public ResponseEntity<DocumentoDTO> registrarSubidaDeDocumento(@Valid @RequestBody DocumentoDTO nuevoDocumento,
           @PathVariable Long operadorId, @PathVariable Long camionId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.documentoService.registrarSubidaDocumento(nuevoDocumento, operadorId, camionId));
    }

    @DeleteMapping("/{documentoId}")
    public ResponseEntity<Void> eliminarDocumentoFisicoYLogico(@PathVariable Long documentoId){
        this.documentoService.eliminarDocumentoFisicoYLogico(documentoId);
        return ResponseEntity.noContent().build();
    }
}
