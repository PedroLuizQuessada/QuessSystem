package controle.enums;

public enum OpcaoComboEnum {
    USUARIO("Escolha um usuário"),
    DEPARTAMENTO("Escolha um departamento"),
    GRUPO("Escolha um grupo"),
    PERMITIDOS("Escolha os permitidos"),
    CAMPO("Escolha um tipo de campo"),
    SEM_GERENTE("Sem gerente"),
    SEM_LIDER("Sem líder");

    private final String descricao;

    OpcaoComboEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
