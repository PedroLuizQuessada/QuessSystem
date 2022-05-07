package controle.enums;

public enum OpcaoHomeEnum {
    ATUALIZARCREDENCIAIS("Atualizar credenciais"),
    USUARIOS("Usuários"),
    DEPARTAMENTOS("Departamentos"),
    GRUPOS("Grupos"),
    AUTOMACOES("Automações"),
    CADASTROS("Cadastros");

    private final String descricao;

    OpcaoHomeEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
