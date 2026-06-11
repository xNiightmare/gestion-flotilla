package com.grandedev.gestionflotilla.fileManager;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.file-storage")
public class FileStorageProperties {

    private String basePath;
    private Set<String> allowedMimeTypes;
    private long maxFileSize;
}