package com.grandedev.gestionflotilla.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.grandedev.gestionflotilla.service.VencimientoService;

@Component
public class VencimientoScheduler {

    private static final Logger log = LoggerFactory.getLogger(VencimientoScheduler.class);
    private final VencimientoService vencimientoService;

    public VencimientoScheduler(VencimientoService vencimientoService) {
        this.vencimientoService = vencimientoService;
    }

    @Scheduled(cron = "${app.alertas.cron:0 0 8 * * *}", zone = "${app.alertas.zone:America/Mexico_City}")
    public void generarAlertasDiarias() {
        int procesados = vencimientoService.evaluarVencimientos();
        log.info("Evaluación de vencimientos terminada: {} documentos en umbral", procesados);
    }
}
