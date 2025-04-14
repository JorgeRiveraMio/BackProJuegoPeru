package com.projuegoperu.BackProJuegoPeru.Services;


import com.projuegoperu.BackProJuegoPeru.Models.Templates.MessageHTML;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")//inyecta el valor del correo que se encuentra en el aplication.properties
    private String sender;

    //recibe la instancia JavaMailSender que es para construir y enviar correos
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    //se encarga de generar un correo con el codigo de confirmacion y enviarlo al usuario
    public String sendMessage(String emailUser) {

        MimeMessage message = javaMailSender.createMimeMessage();//se utiliza para crear un mensaje MIME que es para enviar correos con el contenido en formato HTML
        String content = MessageHTML.TEMPLATE_PRUEBA;//se obtiene la plantilla HTML del mensaje a mostrar
        String code = generateRandomCode(); //genera el codigo de 6 digitos

        try {
            message.setSubject("Confirmación de Registro!!");//el asunto del correo
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailUser);//se configura el correo al que va ser enviado

            // Lógica para enviar códigos en las etiquetas <p> del HTML
            int index = 0;
            for (int i = 0; i < code.length(); i++) {
                content = setCodeInTemplate(content, index, String.valueOf(code.charAt(i)));
                index++;
            }

            helper.setText(content, true);//confirma que el contenido es HTML
            helper.setFrom(sender);// establece de que correo se esta enviado
            javaMailSender.send(message);// se envia el correo

            return code;  // Retorna el código generado

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    private String generateRandomCode() {
        Random random = new Random();
        int randomCode = 100000 + random.nextInt(900000); // Genera un número entre 100000 y 999999
        return String.valueOf(randomCode);
    }
    //reemplaza los {0}, {1}, etc del HTML con los digitos del codigo aleatorio
    private String setCodeInTemplate(String templateCode, int index, String number) {
        return templateCode.replace("{" + index + "}", number);
    }

}
