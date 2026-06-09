package com.grandedev.gestionflotilla.fileManager;

import org.springframework.stereotype.Service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class LocalFileStorageService {
    private final FileStorageProperties properties;
    private final Path rootPath;

    public LocalFileStorageService(FileStorageProperties properties){
        this.properties = properties;
        this.rootPath = Paths.get(properties.basePath());
    }

    public String storeFile(InputStream inputStream, String originalName) throws IOException {
        LocalDate today = LocalDate.now();
        Path dateDirectory = rootPath.resolve(
                today.getYear() + File.separator +
                        String.format("%02d", today.getMonthValue() + File.separator +
                        String.format("%02d", today.getDayOfMonth()))
        );

        Files.createDirectories(dateDirectory);
        String ext = getFileExtension(originalName);
        String storedName = UUID.randomUUID() + (ext.isEmpty() ? "": "." + ext);
        Path filePath = dateDirectory.resolve(storedName);

        try(OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW)){
            StreamUtils.copy(inputStream,outputStream);
        }

        return rootPath.relativize(filePath).toString();

    }

    public Resource getFileResource(String storedPath) throws IOException{
        Path filePath = rootPath.resolve(storedPath).normalize().toAbsolutePath();
        Path normalizedRooth = rootPath.normalize().toAbsolutePath();

        if(!filePath.startsWith(normalizedRooth)){
            throw new SecurityException("Acceso denegado!!");
        }

        if(!Files.exists(filePath)){
            throw new FileNotFoundException("Archivo inexistente");
        }

        return new UrlResource(filePath.toUri());
    }
    private String getFileExtension(String fileName){
        int lastDot = fileName.lastIndexOf(".");
        return lastDot == -1 ? "": fileName.substring(lastDot + 1);
    }

}
