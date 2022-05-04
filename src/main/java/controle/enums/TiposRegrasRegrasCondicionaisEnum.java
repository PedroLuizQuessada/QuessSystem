package controle.enums;

public enum TiposRegrasRegrasCondicionaisEnum {
    VISIBILIDADE("Visibilidade"),
    BLOQUEIO("Bloqueio"),
    OBRIGATORIEDADE("Obrigatoriedade");

    private final String descricao;

    TiposRegrasRegrasCondicionaisEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
