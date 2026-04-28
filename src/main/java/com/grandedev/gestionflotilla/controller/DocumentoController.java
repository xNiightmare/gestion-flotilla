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

import com.grandedev.gestionflotilla.dto.DocumentoDTO;
import com.grandedev.gestionflotilla.service.DocumentoService;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;

    DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @PostMapping
    DocumentoDTO nuevoDocumento(@RequestBody DocumentoDTO documento) {
        return this.documentoService.crearDocumento(documento);
    }

    @GetMapping("/{id}")
    DocumentoDTO obtenerDocumentoPorId(@PathVariable Long id) {
        return this.documentoService.buscarDocumentoPorId(id);
    }

    @PutMapping("/{id}")
    DocumentoDTO actualizarDocumento(@RequestBody DocumentoDTO documento, @PathVariable Long id) {
        return this.documentoService.actualizarDocumento(documento, id);
    }

    @GetMapping
    public List<DocumentoDTO> listarDocumentos() {
        return this.documentoService.listarDocumentos();
    }

    @GetMapping("/camiones/{camionId}")
    public List<DocumentoDTO> listarDocumentosPorCamion(@PathVariable Long camionId) {
        return this.documentoService.listarDocumentoPorCamion(camionId);
    }

    @GetMapping("/operadores/{operadorId}")
    public List<DocumentoDTO> listarDocumentosPorOperador(@PathVariable Long operadorId) {
        return this.documentoService.listarDocumentosPorOperador(operadorId);
    }

    @PutMapping("/{documentoId}/operadores/{operadorId}")
    public DocumentoDTO asignarDocumentoAOperador(@PathVariable Long documentoId, @PathVariable Long operadorId) {
        return this.documentoService.asignarDocumentoAOperador(documentoId, operadorId);
    }

    @PutMapping("/{documentoId}/camiones/{camionId}")
    public DocumentoDTO asignarDocumentoACamion(@PathVariable Long documentoId, @PathVariable Long camionId) {
        return this.documentoService.asignarDocumentoACamion(documentoId, camionId);
    }

    @PostMapping("/operadores/{operadorId}/camiones/{camionId}")
    public DocumentoDTO registrarSubidaDeDocumento(@RequestBody DocumentoDTO nuevoDocumento,
            @PathVariable Long operadorId, @PathVariable Long camionId) {
        return this.documentoService.registrarSubidaDocumento(nuevoDocumento, operadorId, camionId);
    }

    @DeleteMapping("/{documentoId}")
    public void eliminarDocumentoFisicoYLogico(@PathVariable Long documentoId) {
        this.documentoService.eliminarDocumentoFisicoYLogico(documentoId);
    }
}
