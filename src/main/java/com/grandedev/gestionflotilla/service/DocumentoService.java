package com.grandedev.gestionflotilla.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
//I <3 Ariana Grande <3 <3 AG8 <3
import org.springframework.stereotype.Service;

import com.grandedev.gestionflotilla.model.Camion;
import com.grandedev.gestionflotilla.model.Documento;
import com.grandedev.gestionflotilla.model.Operador;
import com.grandedev.gestionflotilla.repository.CamionRepository;
import com.grandedev.gestionflotilla.repository.DocumentoRepository;
import com.grandedev.gestionflotilla.repository.OperadorRepository;

@Service
public class DocumentoService {
    private final DocumentoRepository documentoRepository;
    private final OperadorRepository operadorRepository;
    private final CamionRepository camionRepository;

    public DocumentoService(DocumentoRepository documentoRepository, OperadorRepository operadorRepository,
            CamionRepository camionRepository) {
        this.documentoRepository = documentoRepository;
        this.operadorRepository = operadorRepository;
        this.camionRepository = camionRepository;
    }

    public Documento buscarDocumentoPorId(Long id) {
        return this.documentoRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Documento con id: " + id + " no ha sido encontrado"));
    }

    public Documento crearDocumento(Documento nuevoDocumento) {
        return this.documentoRepository.save(nuevoDocumento);
    }

    public List<Documento> listarDocumentos() {
        return this.documentoRepository.findAll();
    }

    public Documento actualizarDocumento(Documento documentoActualizado, Long id) {
        return this.documentoRepository
            .findById(id)
            .map(documento -> {
                //documento.setFechaSubida(documentoActualizado.getFechaSubida());
                documento.setNombreArchivo(documentoActualizado.getNombreArchivo());
                documento.setRutaArchivo(documentoActualizado.getRutaArchivo());
                documento.setTipoDocumento(documentoActualizado.getTipoDocumento());

                return this.documentoRepository.save(documento);
            })
            .orElseThrow(() -> new IllegalArgumentException("Documento con id: " + id + " no ha sido encontrado"));
    }

    public boolean documentoExistePorId(Long id) {
        return this.documentoRepository.existsById(id);
    }

    public List<Documento> listarDocumentosPorOperador(Long operadorId) {
        this.operadorRepository
            .findById(operadorId)
            .orElseThrow(() -> new IllegalArgumentException("Operador con id: " + operadorId + " no ha sido encontrado"));

        return this.documentoRepository.findByOperadorId(operadorId);
    }

    public List<Documento> listarDocumentoPorCamion(Long camionId) {
        this.camionRepository
            .findById(camionId)
            .orElseThrow(() -> new IllegalArgumentException("Camion con id: " + camionId + " no ha sido encontrado"));

        return this.documentoRepository.findByCamionId(camionId);
    }

    public Documento asignarDocumentoAOperador(Long documentoId, Long operadorId) {
        Documento documento = this.buscarDocumentoPorId(documentoId);
        Operador operador = this.operadorRepository
            .findById(operadorId)
            .orElseThrow(() -> new IllegalArgumentException("Operador con id: " + operadorId + " no ha sido encontrado"));

        documento.setOperador(operador);
        return this.documentoRepository.save(documento);
    }

    public Documento asignarDocumentoACamion(Long documentoId, Long camionId) {
        Documento documento = this.buscarDocumentoPorId(documentoId);
        Camion camion = this.camionRepository
            .findById(camionId)
            .orElseThrow(() -> new IllegalArgumentException("Camion con id: " + camionId + " no ha sido encontrado"));

        documento.setCamion(camion);
        return this.documentoRepository.save(documento);
    }

    public Documento registrarSubidaDocumento(Documento nuevoDocumento, Long operadorId, Long camionId) {
        if (operadorId != null) {
            Operador operador = this.operadorRepository
                .findById(operadorId)
                .orElseThrow(() -> new IllegalArgumentException("Operador con id: " + operadorId + " no ha sido encontrado"));
            nuevoDocumento.setOperador(operador);
        }

        if (camionId != null) {
            Camion camion = this.camionRepository
                .findById(camionId)
                .orElseThrow(() -> new IllegalArgumentException("Camion con id: " + camionId + " no ha sido encontrado"));
            nuevoDocumento.setCamion(camion);
        }

        if (nuevoDocumento.getFechaSubida() == null) {
            nuevoDocumento.setFechaSubida(LocalDateTime.now());
        }

        return this.documentoRepository.save(nuevoDocumento);
    }

    public void eliminarDocumentoFisicoYLogico(Long documentoId) {
        Documento documento = this.buscarDocumentoPorId(documentoId);

        if (documento.getRutaArchivo() != null && !documento.getRutaArchivo().isBlank()) {
            try {
                Files.deleteIfExists(Path.of(documento.getRutaArchivo()));
            } catch (IOException e) {
                throw new IllegalStateException("No se pudo eliminar el archivo del documento con id: " + documentoId, e);
            }
        }

        this.documentoRepository.delete(documento);
    }
    
}
