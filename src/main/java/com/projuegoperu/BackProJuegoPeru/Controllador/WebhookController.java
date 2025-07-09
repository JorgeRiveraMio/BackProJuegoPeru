package com.projuegoperu.BackProJuegoPeru.Controllador;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projuegoperu.BackProJuegoPeru.Services.PagoService;


@RestController
@RequestMapping("/api/webhook")
public class WebhookController {
    private final PagoService pagoService;

    public WebhookController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<String> recibirWebhook(@RequestBody Map<String, Object> payload) {
        try {
        String tipo = (String) payload.get("type");

            if ("payment".equals(tipo)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) payload.get("data");
                Long paymentId = Long.parseLong(data.get("id").toString());

                pagoService.procesarPago(paymentId);
            }

            return ResponseEntity.ok("Webhook recibido");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error procesando webhook");
        }
    }
}
