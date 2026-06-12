package com.grandedev.gestionflotilla.controller;

import java.io.IOException;
import java.util.List;

import com.grandedev.gestionflotilla.fileManager.FileService;
import com.grandedev.gestionflotilla.model.TipoDocumento;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.grandedev.gestionflotilla.dto.DocumentoDTO;
import com.grandedev.gestionflotilla.service.DocumentoService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;
    private final FileService fileService;

    DocumentoController(DocumentoService documentoService,
                        FileService fileService) {
        this.documentoService = documentoService;
        this.fileService = fileService;
    }

    @GetMapping
    ResponseEntity<List<DocumentoDTO>> listarDocumentos(){return ResponseEntity.ok(this.documentoService.listarDocumentos());}

    @GetMapping("/{id}")
    public ResponseEntity<DocumentoDTO> obtenerDocumentoPorId(@PathVariable Long id){
        return ResponseEntity.ok(this.documentoService.buscarDocumentoPorId(id));
    }

    @GetMapping("/camiones/{camionId}")
    public ResponseEntity<List<DocumentoDTO>> listarDocumentoPorCamion(@PathVariable Long camionId){
        return ResponseEntity.ok(this.documentoService.listarDocumentoPorCamion(camionId));
    }

    @GetMapping("/operadores/{operadorId}")
    @PreAuthorize("hasRole('ADMIN') or #operadorId == authentication.principal.operador.id")
    public ResponseEntity<List<DocumentoDTO>> listarDocumentoPorOperador(@PathVariable Long operadorId){
        return ResponseEntity.ok(this.documentoService.listarDocumentosPorOperador(operadorId));
    }

    @GetMapping("/{docId}/download")
    @PreAuthorize("@securityService.esMiDocumento(#docId, authentication)")
    public ResponseEntity<Resource> descargarDocumento(@PathVariable Long docId)
            throws IOException {

        DocumentoDTO dto = documentoService.buscarDocumentoPorId(docId);
        Resource resource = fileService.getFileResource(docId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + dto.getNombreArchivo() + "\""
                )
                .contentType(MediaType.parseMediaType(dto.getMimeType()))
                .contentLength(dto.getTamanioArchivo())
                .body(resource);

    }

    @DeleteMapping("/{documentoId}")
    public ResponseEntity<Void> eliminarDocumentoFisicoYLogico(@PathVariable Long documentoId){
        this.documentoService.eliminarDocumentoFisicoYLogico(documentoId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(
            value ="/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentoDTO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam TipoDocumento tipoDocumento,
            @RequestParam(required = false) Long idOperador,
            @RequestParam(required = false) Long idCamion) throws IOException {

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            this.fileService.uploadFile(file,
                                                        tipoDocumento,
                                                        idOperador,
                                                        idCamion)
                    );



    }


}
