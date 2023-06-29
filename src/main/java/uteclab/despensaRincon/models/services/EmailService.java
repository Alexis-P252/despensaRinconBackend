package uteclab.despensaRincon.models.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ClienteRegularService clienteRegularService;

    public EmailService() {
    }

    public String mandarEmail(String[] destinatarios, String titulo, String contenido){

        SimpleMailMessage mensaje = new SimpleMailMessage();

        mensaje.setTo(destinatarios);
        mensaje.setSubject(titulo);
        mensaje.setText(contenido);

        try{
            javaMailSender.send(mensaje);
        }catch(MailException e){
            return e.getMessage();
        }

        return "Enviado correctamente";

    }

    public String mandarEmailHtml(String titulo, String contenido) {
        List<String> destinatarios = clienteRegularService.findAllCorreos();
        MimeMessage message = javaMailSender.createMimeMessage();
        InternetAddress[] direcciones = new InternetAddress[destinatarios.size()];

        for (int i = 0; i < destinatarios.size(); i++) {
            try {
                if (destinatarios.get(i) != null) {
                    direcciones[i] = new InternetAddress(destinatarios.get(i));
                }
            } catch (MessagingException e) {
                return "Error al crear direcciones de correo";
            }
        }

        try {
            message.setFrom(new InternetAddress("despensarincon@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, direcciones);
            message.setSubject(titulo);
            String htmlContent = crearCorreo(titulo, contenido);
            message.setContent(htmlContent, "text/html; charset=utf-8");

            // Crear y ejecutar un nuevo hilo para el envío del correo
            Thread thread = new Thread(() -> {
                javaMailSender.send(message);
            });
            thread.start();
        } catch (MessagingException e) {
            return "Error al enviar los correos";
        }

        return "Correos enviados correctamente";
    }

    public String crearCorreo(String titulo, String descripcion) {
    return "<!DOCTYPE html>\n" +
            "<html>\n" +
            "  <head>\n" +
            "    <style>\n" +
            "      /* Estilos generales */\n" +
            "      body {\n" +
            "        font-family: Arial, sans-serif;\n" +
            "      }\n" +
            "\n" +
            "      .container {\n" +
            "        max-width: 600px;\n" +
            "        margin: 0 auto;\n" +
            "        padding: 20px;\n" +
            "      }\n" +
            "\n" +
            "      /* Estilos del encabezado */\n" +
            "      .header {\n" +
            "        text-align: center;\n" +
            "        margin-bottom: 20px;\n" +
            "      }\n" +
            "\n" +
            "       .logo {\n" +
            "        display: flex;\n" +
            "        align-items: center;\n" +
            "        justify-content: center;\n" +
            "        height: 120px;\n" +
            "        width: 120px;\n" +
            "        background-color: #f44336;\n" +
            "        border-radius: 50%;\n" +
            "        box-shadow: 0 0 20px rgba(0, 0, 0, 0.8);\n" +
            "        margin: 0 auto;\n" +
            "        margin-bottom: 20px;\n" +
            "      }\n" +
            "\n" +
            "      .initial {\n" +
            "        font-family: 'Arial', sans-serif;\n" +
            "        font-size: 50px;\n" +
            "        color: white;\n" +
            "        text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.6);\n" +
            "        transform: rotate(-15deg);\n" +
            "        transition: transform 0.3s ease;\n" +
            "      }\n" +
            "\n" +
            "      .initial:first-child {\n" +
            "        color: #FFFFFF;\n" +
            "      }\n" +
            "\n" +
            "      .initial:last-child {\n" +
            "        color: #FFFFFF;\n" +
            "      }\n" +
            "\n" +
            "      .logo:hover .initial {\n" +
            "        transform: rotate(15deg);\n" +
            "      }\n" +
            "\n" +
            "      /* Estilos del cuerpo del correo */\n" +
            "      .content {\n" +
            "        background-color: #f9f9f9;\n" +
            "        padding: 20px;\n" +
            "        border-radius: 5px;\n" +
            "      }\n" +
            "\n" +
            "      /* Estilos del pie de página */\n" +
            "      .footer {\n" +
            "        text-align: center;\n" +
            "        margin-top: 20px;\n" +
            "        font-size: 12px;\n" +
            "        color: #888;\n" +
            "      }\n" +
            "    </style>\n" +
            "  </head>\n" +
            "  <body>\n" +
            "    <div class=\"container\">\n" +
            "      <div class=\"header\">\n" +
            "      </div>\n" +
            "      <div class=\"content\">\n" +
            "        <h1 style=\"color: #f44336;\">Despensa Rincón</h1>\n" +
            "        <p>" + titulo  + "</p>\n" +
            "        <p>" + descripcion + "</p>\n" +
            "      </div>\n" +
            "      <div class=\"footer\">\n" +
            "        |  Despensa Rincón  |\n" +
            "      </div>\n" +
            "    </div>\n" +
            "  </body>\n" +
            "</html>";
    }


}