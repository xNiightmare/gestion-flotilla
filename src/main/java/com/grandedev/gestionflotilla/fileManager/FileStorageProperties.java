package com.grandedev.gestionflotilla.fileManager;

import org.springframework.boot.context.properties.ConfigurationProperties;
import java.util.Set;

@ConfigurationProperties(prefix = "app.file-storage")
public record FileStorageProperties(
        String basePath,
        Set<String> allowedMimeTypes,
        long maxFileSize
) {
}
