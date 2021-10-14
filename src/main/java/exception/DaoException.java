package exception;

public class DaoException extends Exception{
    public DaoException() {
        super("Falha ao acessar dados");
    }
}
