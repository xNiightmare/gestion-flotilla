package com.grandedev.gestionflotilla.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.seed.admin")
@Getter @Setter
public class AdminSeedProperties {
    private String username;
    private String password;
    private String email;
}
