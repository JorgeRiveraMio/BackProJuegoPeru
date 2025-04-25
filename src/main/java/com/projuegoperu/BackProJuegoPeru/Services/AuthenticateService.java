package com.projuegoperu.BackProJuegoPeru.Services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateService {
    private final EmailService mailManager;


    public AuthenticateService(EmailService mailManager) {
        this.mailManager = mailManager;
    }
    @Async
    public String sendMessageUser(String email) {
        return mailManager.sendMessage(email);  // Retorna el código de verificación
    }
}
