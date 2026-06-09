package com.grandedev.gestionflotilla.fileManager;

import com.grandedev.gestionflotilla.dto.DocumentoDTO;
import com.grandedev.gestionflotilla.mapper.Mapper;
import com.grandedev.gestionflotilla.model.Documento;
import com.grandedev.gestionflotilla.model.TipoDocumento;
import com.grandedev.gestionflotilla.repository.DocumentoRepository;
import com.grandedev.gestionflotilla.service.DocumentoService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class FileService {
    private final FileStorageProperties properties;
    private final DocumentoRepository documentoRepository;
    private final LocalFileStorageService storageService;

    public FileService(FileStorageProperties properties, DocumentoRepository documentoRepository, LocalFileStorageService storageService) {
        this.properties = properties;
        this.documentoRepository = documentoRepository;
        this.storageService = storageService;
    }

    public DocumentoDTO uploadFile(MultipartFile file, String ownerId, TipoDocumento tipoDocumento) throws IOException {
        validateFile(file);

        String storagePath;
        try(InputStream inputStream = file.getInputStream()){
            storagePath = storageService.storeFile(inputStream, file.getOriginalFilename());
        }

        Documento documento = new Documento();
        documento.setTipoDocumento(tipoDocumento);
        documento.setRutaArchivo(file.getOriginalFilename());
        documento.setRutaArchivo(storagePath);
        documento.setFechaSubida(LocalDateTime.now());
        documento.setTamanioArchivo(file.getSize());
        documento.setMimeType(file.getContentType());

        return Mapper.toDocumentoDTO(this.documentoRepository.save(documento));

    }

    public Resource getFileResource(Long docId) throws IOException{
        return storageService.getFileResource(documentoRepository.getReferenceById(docId););
    }



    private void validateFile(MultipartFile file){
        if(file.isEmpty()){
            throw new IllegalArgumentException("Archivo vacio!!");
        }

        String mimeType = file.getContentType();
        if(mimeType == null || !properties.allowedMimeTypes().contains(mimeType)){
            throw new IllegalArgumentException("tipo mime invalido!!!");
        }
    }
}
