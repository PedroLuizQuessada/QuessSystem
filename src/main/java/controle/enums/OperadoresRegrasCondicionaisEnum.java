package controle.enums;

public enum OperadoresRegrasCondicionaisEnum {
    IGUAL("=="),
    DIFERENTE("!=");

    private final String descricao;

    OperadoresRegrasCondicionaisEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
