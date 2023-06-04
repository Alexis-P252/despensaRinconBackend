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

    public String crearCorreo(String titulo, String descripcion) {
        String logo = "https://lh3.googleusercontent.com/jRX7_1fizNhlO6HSXjbseN5Y5zn8d86Igvd47QENMmCfo2gDQi2o2RzdGRLb9in5Q4ug0idFlUKcVtr0rg6-WEhPmbyECO4tRI47SspRpOnjBxM-MEc3XED_HCoeFcsLjZbQ8ncEs6yPTKdOf3R3e7oL--TScR2w-4huLhWFiOl_su15zckgqDkOr3GhfOYCuhY9PN5ulFmrkgjI2emetnaoIF4vt6ITJHWqjKfi8-3unbJrbuzJ6hUmeiJd8NNQHk5OCkMv3o10BJzDelaIyzpt8huXhKdawdvtiRqpBRhJD3lBAsh4sLW3I21KW6pqMjYcga72YVq3wwqPWBUiCiYE8gAqD0CZpf-IubP9kGMpvAGygzzL_paUt4g-bdjatUJvOoFhFORulbXMiOXISxfKl2B4GE7J3dIjA0JEapIn4FMnwPDLQoM0e5PeBcx0I9a5OLhuKjXdRAzJpPIo9xBr_TV1KUxiGyUurGRLFhSumQ_EbPsoRm10ZGqldLsEiv8kIJUV2Ghsz14RmGIKLJy5gqbg-KUxvtIPXOicgWS2-u9zn0A7qYpm7KOcJMLxEIw38QvZcsj_qhWdgERWJyMEXRT_YbALWkzDAvREB1HupIYgOoyxPbIp8rEttAk51FmeIqosApDtvpfYeMH9JJi8gutuvIvn4LngPB38p0IUqMjsjseJo9Fy3lG_9-aYHrwLAaSCEAm_qH0_x6v0cIoFRA0tLS0Z1HzVtM4zihHVvhDoNJdzcC64gNi-TLQxaHnL42SMy4nOiPB7lTne-VeHkdLGbOoBgoIylloFAXqcAubMGDDUOBAvI6SZpsCzn0Fd5WlTTPy8fvV5HpMQ8xCmIjPObvehC1dxns2mjfZ4zva1aSO-VH0e4cKuijsmgHIARqIR59H_QvVB5Bdnm-I3FYEAhcWX5tXgc_jwdd4=w500-h201-s-no?authuser=0";
    return "<!DOCTYPE html>"+
            "<html lang='en' xmlns='http://www.w3.org/1999/xhtml' xmlns:o='urn:schemas-microsoft-com:office:office'>"+
            "<head>"+
                "<meta charset='UTF-8'>"+
                "<meta name='viewport' content='width=device-width,initial-scale=1'>" +
                "<meta name='x-apple-disable-message-reformatting'>" +
                "<title></title>"+
                "<style>"+
                "table, td, div, h1, p {font-family: Arial, sans-serif;}"+
                "</style>"+
            "</head>"+
            "<body style='margin:0;padding:0;'>"+
                "<table role='presentation' style='width:100%;border-collapse:collapse;border:0;border-spacing:0;background:#ffffff;'>"+
                    "<tr>"+
                        "<td align='center' style='padding:0;'>"+
                            "<table role='presentation' style='width:602px;border-collapse:collapse;border:1px solid #cccccc;border-spacing:0;text-align:left;'>"+
                                "<tr>"+
                                    "<td align='center' style='padding:40px 0 30px 0; background-color:#EFF2F2'>"+
                                        "<img src='"+logo+"' alt='' width='300px' style='height:auto;display:block;' >"+
                                    "</td>"+
                                "</tr>"+
                                "<tr>"+
                                    "<td style='padding:36px 30px 42px 30px;'>"+
                                        "<table role='presentation' style='width:100%;border-collapse:collapse;border:0;border-spacing:0; text-align: center;'>"+
                                            "<tr>"+
                                                "<td style='padding:0 0 36px 0;color:#153643;'>"+
                                                    "<h1 style=' color:#353956; font-size:24px;margin:0 0 20px 0;font-family:Arial,sans-serif;'>"+
                                                    titulo +
                                                    "</h1>"+
                                                   " <hr>"+
                                                    "<br>"+
                                                    "<p style='color:#353956; margin:0 0 12px 0;font-size:16px;line-height:24px;font-family:Arial,sans-serif; text-align: left;'>"+
                                                    descripcion+
                                                    "</p>"+
                                                    "<br>"+
                                                    "<hr>"+
                                                "</td>"+
                                            "</tr>"+
                                        "</table>"+
                                    "</td>"+
                                "</tr>"+
                                "<tr>"+
                                    "<td style='padding:30px;background-color:#EFF2F2;'>"+
                                        "<table role='presentation' style='width:100%;border-collapse:collapse;border:0;border-spacing:0;font-size:9px;font-family:Arial,sans-serif;'>"+
                                            "<tr>"+
                                                "<td style='padding:0;width:50%;' align='left'>"+
                                                    "<p style='margin:0;font-size:14px;line-height:16px;font-family:Arial,sans-serif;color:#353956;'>"+
                                                        "<strong>Despensa Rincón</strong> <br/>"+
                                                    "</p>"+
                                               " </td>"+
                                            "</tr>"+
                                        "</table>"+
                                    "</td>"+
                                "</tr>"+
                            "</table>"+
                        "</td>"+
                    "</tr>"+
                "</table>"+
            "</body>"+
            "</html>";
    }

        /*


        String logo = "https://img.freepik.com/vector-premium/hola-hola-letras-frase-aisladas-blanco-vector-diseno-efecto-texto-colorido-texto-o-inscripciones-ingles-diseno-moderno-creativo-tiene-colores-rojo-naranja-amarillo_7280-7768.jpg?w=2000";
        String logo2 = "https://lh3.googleusercontent.com/pw/AJFCJaVsdd0UasvuLahNLwNPmtiF9QTJrvlEmIR6EZb3OYrC1qwZ7vZnHSbNGhbovMMuNUlRnP1GHQ-T55uAlSvAdNDLmq2QHXUOIJHIW_SV12h8-AHfu2BIy_k2LUGcafuSuDF3OqOVajbarMSc3wyWr0rnk6cESicnInYCJ9m0oaceAMMtN4aucC8pipE_Lp27qHqFWtJpFAgs9A0yCz8Euph-UylvsYMQ6l2YE9Go9RPE2G2bX8riSR0A_hlaCVUAXxDNfjiDuamcSW-rFgtzM0VTZz91RDoi-z1T1ihjl7bWVmp0NDJUedIeXQFQFB40zwa8itCbm-rOPXIbhgnnJ0uxH1hFzJ2ck_WBi2-cqgrSFij0B8-vjg4H5HELWnQKYo4ckQNa5Q0e67W5bc9-jXNUSxkXqZ0GnyeBAR5evxs9lvtuylNbvdqcZtPt4NVXc1suPRXDt3zeRAnU_HhcW_-ZDX5TBwU0qhhUOf9d4ScLlXq0y4Q7pbnAGkj-ngOi71TqjQD-n0v88l8de_DSNa-550eAUArhAaAWJr8MlWiJMp9tHtbSNXrErubydf3QbY9tKTUNKiOf1uAPhAfadGCsGGcrgEU803OrX1iEeAc3TiWl0c4dXxkhSUDd-FIhJdzmCP1lHZ87oEVRjWIa4yqZUWBMCoZ955biQOSxnv3_k_e50fyos7ShRMrQF78yj5PXgyuhtjinmelnqX_0mnFPVGnjV-uhO0Nn57zvwRrQn-9txLBpwbqFR3YIdE7mEqSTIy2QyYSb8OPv166qBpNnID1Mm-sSMmlObAOihGIjTjenZkX1RigQLoMQ96P0uIWeIqYtIBqCETg7ATTNbpU_HTtR0JoNFmEWeBkgtA2QLV_cHM728DrT0O7KGkr37_JqONK97uJPDseVXhJyug=w700-h933-s-no?authuser=0";
        return " <div class='container'style='font-family: Calibri, Arial, sans-serif; border-radius: 10px; background-color: #f7f7f7; overflow: hidden; max-width: 600px; margin: 0 auto;'>"+
                "<div class= 'header' style='background-color: #eeeeee; padding: 20px; display: flex; align-items: center;'>"+
                "<h1>"+titulo+"</h1>"+
                "<iframe src='https://drive.google.com/file/d/1nCEDigmGWNdI3sBbJknRSeNPnvk5WxQt/preview' width='640' height='480' allow='autoplay'></iframe>"+
                "<img style = 'width: 100px; height: auto; margin-right: 20px; float: right;' src='"+logo+"' alt='"+"logo"+"'>"+
                "</div>"+
                "<div class='body' style='background-color: #f5f5f5;'>"+
                "<h3>"+descripcion+"</h3>"+
                "</div>"+
                "<div class='footer' style = 'background-color: #eeeeee; padding: 10px; display: flex; justify-content: center; align-items: center;'>"+
                "<img style ='float: center ;width: 100px; height: auto;' src='"+logo2+"' alt='Despensa Rincon'>"+
                "</div>"+
                "</div>";

    }


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