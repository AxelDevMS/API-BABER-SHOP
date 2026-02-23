package ams.dev.api.barber_shop.service.impl;

import ams.dev.api.barber_shop.exceptions.BusinessException;
import ams.dev.api.barber_shop.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;

@Service
public class EMailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String fromEmail;  // Este es FIJO (ej: no-reply@tuaplicacion.com)

    @Value("${app.mail.fromName}")
    private String fromName;    // Este es FIJO (ej: "Mi Aplicación")

    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * Envía credenciales al email del usuario (destinatario dinámico)
     * @param toEmail - Email del destinatario (el que el usuario registró) - ES DINÁMICO
     * @param nombre - Nombre del usuario
     * @param username - Nombre de usuario (generalmente el email)
     * @param password - Contraseña temporal
     */
    @Override
    public void sendEmailCredentialsAcces(String toEmail, String nombre, String username, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // FROM: Es estático (configurado en properties)
            helper.setFrom(fromEmail, fromName);

            // TO: Es DINÁMICO (el email que el usuario registró)
            helper.setTo(toEmail);

            // Asunto del correo
            helper.setSubject("¡Bienvenido a nuestra plataforma! - Credenciales de acceso");

            // Preparar el contexto para la plantilla
            Context context = new Context();
            context.setVariable("nombre", nombre);
            context.setVariable("username", username);
            context.setVariable("password", password);
            context.setVariable("baseUrl", baseUrl);
            context.setVariable("loginUrl", baseUrl + "/login");
            context.setVariable("resetPasswordUrl", baseUrl + "/reset-password");

            // Procesar la plantilla HTML
            String htmlContent = templateEngine.process("email/bodyEmailAccess", context);
            helper.setText(htmlContent, true);

            // Enviar el correo
            mailSender.send(message);

            System.out.println("Correo enviado exitosamente a: " + toEmail); // Log para verificar

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("Error al enviar el correo a " + toEmail + ": " + e.getMessage());
        }

    }
}
