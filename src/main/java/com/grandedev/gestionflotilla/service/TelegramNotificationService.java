package com.grandedev.gestionflotilla.service;

import com.grandedev.gestionflotilla.dto.TelegramMessage;
import com.grandedev.gestionflotilla.notification.NotificationContext;
import com.grandedev.gestionflotilla.notification.NotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@ConditionalOnProperty(
        name = "app.telegram.enabled",
        havingValue = "true"

)
@Order(100)
public class TelegramNotificationService implements NotificationService {

    public final RestClient restClient;
    public final String botToken;

    public TelegramNotificationService(RestClient.Builder builder,
                                       @Value("${app.telegram.bot-token}") String botToken){
        this.restClient = builder.build();
        this.botToken = botToken;
    }

    @Override
    public void notificar(NotificationContext context){
        Long chatId = context.destinatario().getTelegramChatId();

        if(chatId == null)
            return;

        String texto = """
                %s

                %s
                Documento: %s
                Vencimiento: %s
                """.formatted(
                context.titulo(),
                context.mensaje(),
                context.documento().getNombreArchivo(),
                context.documento().getFechaVencimiento()
        );

        restClient.post()
                .uri("https://api.telegram.org/bot{token}/sendMessage",botToken)
                .body(new TelegramMessage(chatId, texto))
                .retrieve()
                .toBodilessEntity();
    }
}
