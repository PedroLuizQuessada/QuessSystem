package controle.enums;

public enum OpcaoHomeEnum {
    USUARIOS("Usuários"),
    DEPARTAMENTOS("Departamentos"),
    GRUPOS("Grupos"),
    CADASTROS("Cadastros");

    private String descricao;

    OpcaoHomeEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
