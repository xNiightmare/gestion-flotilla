package com.grandedev.gestionflotilla.fileManager;

import com.grandedev.gestionflotilla.dto.DocumentoDTO;
import com.grandedev.gestionflotilla.mapper.Mapper;
import com.grandedev.gestionflotilla.model.Camion;
import com.grandedev.gestionflotilla.model.Documento;
import com.grandedev.gestionflotilla.model.Operador;
import com.grandedev.gestionflotilla.model.TipoDocumento;
import com.grandedev.gestionflotilla.repository.CamionRepository;
import com.grandedev.gestionflotilla.repository.DocumentoRepository;
import com.grandedev.gestionflotilla.repository.OperadorRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Service
public class FileService {
    private final FileStorageProperties properties;
    private final DocumentoRepository documentoRepository;
    private final OperadorRepository operadorRepository;
    private final CamionRepository camionRepository;
    private final LocalFileStorageService storageService;

    public FileService(FileStorageProperties properties,
                       DocumentoRepository documentoRepository,
                       OperadorRepository operadorRepository,
                       CamionRepository camionRepository,
                       LocalFileStorageService storageService) {

        this.properties = properties;
        this.documentoRepository = documentoRepository;
        this.operadorRepository = operadorRepository;
        this.camionRepository = camionRepository;
        this.storageService = storageService;
    }

    public DocumentoDTO uploadFile(MultipartFile file,
                                   TipoDocumento tipoDocumento,
                                   Long operadorId,
                                   Long camionId,
                                   LocalDate fechaVencimiento) throws IOException {
        validateFile(file);

        String storagePath;
        try(InputStream inputStream = file.getInputStream()){
            storagePath = storageService.storeFile(inputStream, file.getOriginalFilename());
        }

        Documento documento = new Documento();
        documento.setTipoDocumento(tipoDocumento);
        documento.setNombreArchivo(file.getOriginalFilename());
        documento.setRutaArchivo(storagePath);
        documento.setFechaSubida(LocalDateTime.now());
        documento.setFechaVencimiento(fechaVencimiento);
        documento.setTamanioArchivo(file.getSize());
        documento.setMimeType(file.getContentType());
        asignarRelaciones(documento, operadorId, camionId);

        return Mapper.toDocumentoDTO(this.documentoRepository.save(documento));

    }

    public Resource getFileResource(Long docId) throws IOException{
        Documento documento = documentoRepository
                .findById(docId)
                .orElseThrow();

        return storageService
                .getFileResource(documento
                        .getRutaArchivo());
    }


    //Helpers
    private void validateFile(MultipartFile file){
        if(file.isEmpty()){
            throw new IllegalArgumentException("Archivo vacio!!");
        }

        if(file.getSize() > properties.getMaxFileSize()){
            throw new IllegalArgumentException(
                    "Archivo excede el tamaño permitido"
            );
        }

        String mimeType = file.getContentType();
        if(mimeType == null || !properties.getAllowedMimeTypes().contains(mimeType)){
            throw new IllegalArgumentException("tipo mime invalido!!!");
        }
    }

    private void asignarRelaciones(Documento documento, Long operadorId, Long camionId) {
        if (operadorId != null) {
            Operador operador = this.operadorRepository
                    .findById(operadorId)
                    .orElseThrow(() -> new IllegalArgumentException("Operador con id: " + operadorId + " no ha sido encontrado"));
            documento.setOperador(operador);
        }

        if (camionId != null) {
            Camion camion = this.camionRepository
                    .findById(camionId)
                    .orElseThrow(() -> new IllegalArgumentException("Camion con id: " + camionId + " no ha sido encontrado"));
            documento.setCamion(camion);
        }
    }
}
