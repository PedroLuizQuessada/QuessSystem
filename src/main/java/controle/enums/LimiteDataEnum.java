package controle.enums;

public enum LimiteDataEnum {
    PADRAO("Qualquer data"),
    RETROATIVAS("Somente datas retroativas"),
    POSTERIORES("Somente datas posteriores");

    private final String descricao;

    LimiteDataEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
