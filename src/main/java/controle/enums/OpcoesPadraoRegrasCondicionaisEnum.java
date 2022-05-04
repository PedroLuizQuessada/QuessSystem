package controle.enums;

public enum OpcoesPadraoRegrasCondicionaisEnum {
    TIPOREGRA("Escolha um tipo de regra"),
    CAMPO("Escolha um campo"),
    OPERADOR("Escolha um operador"),
    VALOR("Escolha um valor"),
    GRUPO("Escolha um grupo");

    private final String descricao;

    OpcoesPadraoRegrasCondicionaisEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
