package controle.enums;

public enum LimiteDataHoraEnum {
    PADRAO("Qualquer data e hora"),
    RETROATIVAS("Somente datas retroativas"),
    POSTERIORES("Somente datas posteriores");

    private final String descricao;

    LimiteDataHoraEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
