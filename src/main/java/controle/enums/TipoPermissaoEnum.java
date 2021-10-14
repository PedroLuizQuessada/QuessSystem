package controle.enums;

public enum TipoPermissaoEnum {
    PADRAO("Escolha uma permissão"),
    USUARIO("Usuário"),
    DEPARTAMENTO("Departamento"),
    GRUPO("Grupo"),
    GERENTE("Gerente de departamento"),
    LIDER("Líder de grupo");

    private final String descricao;

    TipoPermissaoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
