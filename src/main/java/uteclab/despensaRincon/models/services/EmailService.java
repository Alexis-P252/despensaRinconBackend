package uteclab.despensaRincon.models.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

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

    public void sendHtmlEmail() throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(new InternetAddress("sender@example.com"));
        message.setRecipients(MimeMessage.RecipientType.TO, "recipient@example.com");
        message.setSubject("Test email from Spring");

        String htmlContent = "<h1>This is a test Spring Boot email</h1>" +
                "<p>It can contain <strong>HTML</strong> content.</p>";
        message.setContent(htmlContent, "text/html; charset=utf-8");

        javaMailSender.send(message);
    }

}
