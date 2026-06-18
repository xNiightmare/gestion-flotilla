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
        this.rootPath = Paths.get(properties.getBasePath());
    }

    public String storeFile(InputStream inputStream, String originalName) throws IOException {
        LocalDate today = LocalDate.now();
        Path dateDirectory = rootPath
                .resolve(String.valueOf(today.getYear()))
                .resolve(String.format("%02d",today.getMonthValue()))
                .resolve(String.format("%02d",today.getDayOfMonth()));

        Files.createDirectories(dateDirectory);
        String ext = getFileExtension(originalName);
        String storedName = UUID.randomUUID() + (ext.isEmpty() ? "": "." + ext);
        Path filePath = dateDirectory.resolve(storedName);

        try(OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE_NEW)){
            StreamUtils.copy(inputStream,outputStream);
        }

        return rootPath
                .relativize(filePath)
                .toString().replace("\\","/");

    }

    public Resource getFileResource(String storedPath) throws IOException{
        Path filePath = rootPath.resolve(storedPath).normalize().toAbsolutePath();
        Path normalizedRoot = rootPath.normalize().toAbsolutePath();

        System.out.println(filePath);
        System.out.println(normalizedRoot);

        if(!filePath.startsWith(normalizedRoot)){
            throw new SecurityException("Acceso denegado!!");
        }

        if(!Files.exists(filePath)){
            throw new FileNotFoundException("Archivo inexistente");
        }

        return new UrlResource(filePath.toUri());
    }

    public void deleteFile(String storedPath) throws IOException{
        Path filePath = rootPath
                .resolve(storedPath)
                .normalize()
                .toAbsolutePath();

        Path normalizeRoot = rootPath.normalize()
                                    .toAbsolutePath();

        if(!filePath.startsWith(normalizeRoot)){
            throw new SecurityException("Acceso denegado!!");
        }

        Files.deleteIfExists(filePath);

    }

    private String getFileExtension(String fileName){
        int lastDot = fileName.lastIndexOf(".");
        return lastDot == -1 ? "": fileName.substring(lastDot + 1);
    }

}
