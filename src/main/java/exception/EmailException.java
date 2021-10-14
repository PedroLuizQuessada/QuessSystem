package exception;

public class EmailException extends Exception{
    public EmailException(){
        super("Falha no serviço de e-mail");
    }
}