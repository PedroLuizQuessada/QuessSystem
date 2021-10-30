package controle.enums;

public enum AcoesCampoCadastroEnum {
    PADRAO("Escolha uma ação"),
    VINCULAR("Vincular"),
    DESVINCULAR("Desvincular"),
    BLOQUEAR("Bloquear"),
    DESBLOQUEAR("Desbloquear"),
    TORNAR_OBRIGATORIO("Tornar obrigatório"),
    TORNAR_NAO_OBRIGATORIO("Tornar não obrigatório"),
    TORNAR_PESQUISAVEL("Tornar pesquisável"),
    TORNAR_NAO_PESQUISAVEL("Tornar não pesquisável"),
    QUEBRAR_LINHA("Quebrar linha"),
    NAO_QUEBRAR_LINHA("Não quebrar linha");

    private final String descricao;

    AcoesCampoCadastroEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
