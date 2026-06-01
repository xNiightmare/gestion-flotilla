package com.grandedev.gestionflotilla.security;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.jwt")
public class SecretKeyProperties {

    private String secretKey;
    private long expirationTime;


}
