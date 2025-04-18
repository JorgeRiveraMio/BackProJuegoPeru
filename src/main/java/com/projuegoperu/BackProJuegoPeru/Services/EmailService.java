package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.Templates.MessageHTML;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.from}") // Correo verificado (el que se ve como remitente)
    private String fromEmail;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String sendMessage(String emailUser) {
        MimeMessage message = javaMailSender.createMimeMessage();
        String content = MessageHTML.TEMPLATE_PRUEBA;
        String code = generateRandomCode();

        try {
            message.setSubject("Confirmación de Registro!!");
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailUser);
            // Insertar los dígitos del código en el HTML
            for (int i = 0; i < code.length(); i++) {
                content = setCodeInTemplate(content, i, String.valueOf(code.charAt(i)));
            }
            helper.setText(content, true);
            helper.setFrom(fromEmail); // Remitente verificado
            javaMailSender.send(message);

            return code;

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }

    private String generateRandomCode() {
        Random random = new Random();
        int randomCode = 100000 + random.nextInt(900000);
        return String.valueOf(randomCode);
    }

    private String setCodeInTemplate(String templateCode, int index, String number) {
        return templateCode.replace("{" + index + "}", number);
    }


public void enviarCorreoCambioPassword(String to, String token) {
    String link = "http://localhost:4200/nuevaContrasena?token=" + token;
    MimeMessage message = javaMailSender.createMimeMessage();

    try {
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Recuperación de contraseña");
        String content = "Haz clic en el siguiente enlace para cambiar tu contraseña: <a href=\"" + link + "\">Cambiar contraseña</a>";
        helper.setText(content, true);
        helper.setFrom(fromEmail);
        javaMailSender.send(message);
    } catch (MessagingException e) {
        throw new RuntimeException("Error al enviar el correo de recuperación: " + e.getMessage(), e);
    }
}

}
