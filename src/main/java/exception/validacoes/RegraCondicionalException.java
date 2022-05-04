package exception.validacoes;

public class RegraCondicionalException extends Exception {
    public RegraCondicionalException(String info) {
        super(String.format("Necessário escolher um %s para adicionar a regra condicional", info));
    }
}
