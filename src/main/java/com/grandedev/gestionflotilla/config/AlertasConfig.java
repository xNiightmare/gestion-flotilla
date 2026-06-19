package com.grandedev.gestionflotilla.config;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlertasConfig {

    @Bean
    Clock alertasClock(@Value("${app.alertas.zone:America/Mexico_City}") String zone) {
        return Clock.system(ZoneId.of(zone));
    }
}
