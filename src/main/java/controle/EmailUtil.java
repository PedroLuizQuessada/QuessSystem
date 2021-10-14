package controle;

import exception.EmailException;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class EmailUtil {
    private final String LETRAS = "abcdefghijklmnopqrstuvwxyz";
    private final char[] CARACTERES = (LETRAS + LETRAS.toUpperCase() + "0123456789").toCharArray();

    private final String usuarioEmail = "quesssystems@gmail.com";

    public String enviarSenha(String destinatario) throws EmailException {
        try {
            Properties prop = System.getProperties();
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", "465");

            Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(usuarioEmail, "mottaquessadalol12");
                }
            });
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuarioEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            message.setSubject("Nova senha");

            String novaSenha = geraSenha();
            message.setText("Sua nova senha é: " + novaSenha);
            Transport.send(message);

            return novaSenha;
        }
        catch (Exception exception){
            exception.printStackTrace();
            throw new EmailException();
        }
    }

    private String geraSenha(){
        StringBuilder senha = new StringBuilder();

        for (int i = 0; i <  8; i++){
            senha.append(CARACTERES[new Random().nextInt(CARACTERES.length)]);
        }

        return senha.toString();
    }
}
