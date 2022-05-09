package exception;

public class ConversaoException extends Exception {
    public ConversaoException(String metodo) {
        super(String.format("Falha na tentativa de conversão no método %s", metodo));
    }
}
