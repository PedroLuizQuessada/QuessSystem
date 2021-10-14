package exception;

public class UsuarioInvalidoException extends Exception {
    public UsuarioInvalidoException(){
        super("Usuário não encontrado");
    }
}
