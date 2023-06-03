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
    public String mandarEmailHtml( String titulo, String contenido){
        List<String> destinatarios = clienteRegularService.findAllCorreos();
        MimeMessage message = javaMailSender.createMimeMessage();
        InternetAddress[] direcciones = new InternetAddress[destinatarios.size()];

        for(int i = 0; i < destinatarios.size(); i++){
            try{
                if(destinatarios.get(i)!=null) {
                    direcciones[i] = new InternetAddress(destinatarios.get(i));
                }
            }catch(MessagingException e){
                return "Error al crear direcciones de correo";
            }

        }
        try{
            message.setFrom(new InternetAddress("despensarincon@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, direcciones);
            message.setSubject(titulo);
            String htmlContent = crearCorreo(titulo, contenido);
            message.setContent(htmlContent, "text/html; charset=utf-8");
            javaMailSender.send(message);
        }catch(MessagingException e){
            return "Error al enviar los correos";
        }
        return "Correos enviados correctamente";
    }

    public String crearCorreo(String titulo, String descripcion){
        return " <div class='container'style='font-family: Calibri, Arial, sans-serif; border-radius: 10px; background-color: #f7f7f7; overflow: hidden; max-width: 600px; margin: 0 auto;'>"+
                "<div class= 'header' style='background-color: #eeeeee; padding: 20px; display: flex; align-items: center;'>"+
                "<h1>"+titulo+"</h1>"+
                "<img style = 'width: 100px; height: auto; margin-right: 20px; float: right;' src='"+"logo"+"' alt='"+"logo"+"'>"+
                "</div>"+
                "<div class='body' style='background-color: #f5f5f5;'>"+
                "<h3>"+descripcion+"</h3>"+
                "</div>"+
                "<div class='footer' style = 'background-color: #eeeeee; padding: 10px; display: flex; justify-content: center; align-items: center;'>"+
                "<img style ='float: center ;width: 100px; height: auto;' src='"+"a"+"' alt='Despensa Rincon'>"+
                "</div>"+
                "</div>";

    }

/*
<head>
    <meta charset="UTF-8">
    <title>Correo Electrónico</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f7f7f7;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #ffffff;
            padding: 20px;
            border: 1px solid #e0e0e0;
        }

        h1 {
            color: #333333;
            font-size: 24px;
            margin-bottom: 20px;
        }

        p {
            color: #555555;
            font-size: 16px;
            margin-bottom: 10px;
        }

        .image-wrapper {
            text-align: center;
            margin-bottom: 20px;
        }

        .image-wrapper img {
            max-width: 100%;
            height: auto;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Bienvenido(a) a nuestro boletín</h1>
        <p>¡Hola!</p>
        <p>Te damos la bienvenida a nuestro boletín informativo. Aquí encontrarás las últimas novedades y promociones.</p>
        <p>¡No te lo pierdas!</p>
        <div class="image-wrapper">
            <img src="ruta_de_la_imagen.jpg" alt="Imagen">
        </div>
    </div>
</body>
</html>
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
*/

}