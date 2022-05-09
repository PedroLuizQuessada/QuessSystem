package controle;

import exception.EmailException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;

public class EmailUtil {
    private final ParametrosUtil parametrosUtil = new ParametrosUtil();

    public void enviarEmail(String destinatario, String assunto, String mensagem, File anexo) throws EmailException {
        try {
            Properties prop = System.getProperties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "465");

            Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(parametrosUtil.getEmailSistema(), parametrosUtil.getSenhaEmailSistema());
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(parametrosUtil.getEmailSistema()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject(assunto);

            if (anexo != null) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(anexo);

                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(mensagem);

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                multipart.addBodyPart(attachmentPart);

                message.setContent(multipart);
            }
            else {
                message.setText(mensagem);
            }

            Transport.send(message);
        }
        catch (Exception exception){
            throw new EmailException();
        }
    }

    public void validarEmail(String email) throws EmailException {
        if(!email.contains("@") || !email.contains(".com")){
            throw new EmailException();
        }
    }
}
