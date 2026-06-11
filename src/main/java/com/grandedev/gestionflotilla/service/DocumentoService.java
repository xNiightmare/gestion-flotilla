package com.grandedev.gestionflotilla.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import com.grandedev.gestionflotilla.fileManager.LocalFileStorageService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.grandedev.gestionflotilla.dto.DocumentoDTO;
import com.grandedev.gestionflotilla.mapper.Mapper;
import com.grandedev.gestionflotilla.model.Camion;
import com.grandedev.gestionflotilla.model.Documento;
import com.grandedev.gestionflotilla.model.Operador;
import com.grandedev.gestionflotilla.repository.CamionRepository;
import com.grandedev.gestionflotilla.repository.DocumentoRepository;
import com.grandedev.gestionflotilla.repository.OperadorRepository;

@Service
public class DocumentoService implements IDocumentoService{
    private final DocumentoRepository documentoRepository;
    private final OperadorRepository operadorRepository;
    private final CamionRepository camionRepository;
    private final LocalFileStorageService storageService;

    public DocumentoService(DocumentoRepository documentoRepository,
                            OperadorRepository operadorRepository,
                            CamionRepository camionRepository,
                            LocalFileStorageService storageService) {
        this.documentoRepository = documentoRepository;
        this.operadorRepository = operadorRepository;
        this.camionRepository = camionRepository;
        this.storageService = storageService;
    }

    private Documento buscarDocumentoEntidadPorId(Long id) {
        return this.documentoRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Documento con id: " + id + " no ha sido encontrado"));
    }

    @Override
    public DocumentoDTO buscarDocumentoPorId(Long id) {
        return Mapper.toDocumentoDTO(this.buscarDocumentoEntidadPorId(id));
    }

    @Override
    public List<DocumentoDTO> listarDocumentos() {
        return this.documentoRepository.findAll().stream().map(Mapper::toDocumentoDTO).toList();
    }


    @Override
    public boolean documentoExistePorId(Long id) {
        return this.documentoRepository.existsById(id);
    }

    @Override
    public List<DocumentoDTO> listarDocumentosPorOperador(Long operadorId) {
        this.operadorRepository
            .findById(operadorId)
            .orElseThrow(() -> new IllegalArgumentException("Operador con id: " + operadorId + " no ha sido encontrado"));

        return this.documentoRepository.findByOperadorId(operadorId).stream().map(Mapper::toDocumentoDTO).toList();
    }

    @Override
    public List<DocumentoDTO> listarDocumentoPorCamion(Long camionId) {
        this.camionRepository
            .findById(camionId)
            .orElseThrow(() -> new IllegalArgumentException("Camion con id: " + camionId + " no ha sido encontrado"));

        return this.documentoRepository.findByCamionId(camionId).stream().map(Mapper::toDocumentoDTO).toList();
    }

    @Override
    public DocumentoDTO asignarDocumentoAOperador(Long documentoId, Long operadorId) {
        Documento documento = this.buscarDocumentoEntidadPorId(documentoId);
        Operador operador = this.operadorRepository
            .findById(operadorId)
            .orElseThrow(() -> new IllegalArgumentException("Operador con id: " + operadorId + " no ha sido encontrado"));

        documento.setOperador(operador);
        return Mapper.toDocumentoDTO(this.documentoRepository.save(documento));
    }

    @Override
    public DocumentoDTO asignarDocumentoACamion(Long documentoId, Long camionId) {
        Documento documento = this.buscarDocumentoEntidadPorId(documentoId);
        Camion camion = this.camionRepository
            .findById(camionId)
            .orElseThrow(() -> new IllegalArgumentException("Camion con id: " + camionId + " no ha sido encontrado"));

        documento.setCamion(camion);
        return Mapper.toDocumentoDTO(this.documentoRepository.save(documento));
    }

    @Override
    @Transactional
    public void eliminarDocumentoFisicoYLogico(Long documentoId) {
        Documento documento =
                this.buscarDocumentoEntidadPorId(documentoId);

        if (documento.getRutaArchivo() != null && !documento.getRutaArchivo().isBlank()) {
            try {
                storageService.deleteFile(
                        documento.getRutaArchivo()
                );
            } catch (IOException e) {
                throw new IllegalStateException("No se pudo eliminar el archivo del documento con id: " + documentoId, e);
            }
        }

        this.documentoRepository.delete(documento);
    }


}
