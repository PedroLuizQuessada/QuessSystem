package controle.enums;

public enum TipoCampoEnum {
    PADRAO("Escolha um tipo de campo"),
    TEXTO("Texto"),
    NUMERICO("Numérico"),
    DATAHORA("Data hora"),
    DATA("Data"),
    CHECKBOX("Checkbox"),
    AREATEXTO("Área de texto"),
    COMBOBOX("Combobox"),
    RADIO("Rádio"),
    AGRUPADOR("Agrupador");

    private final String descricao;

    TipoCampoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
