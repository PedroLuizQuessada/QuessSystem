package controle.enums;

public enum LimiteNumericoEnum {
    PADRAO("Qualquer número"),
    POSITIVOS("Apenas números positivos"),
    NEGATIVOS("Apenas números negativos");

    private final String descricao;

    LimiteNumericoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
