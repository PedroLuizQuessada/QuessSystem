package main;

public class UsuarioLogado {
    private final int id;
    private final String login;
    private final boolean adm;
    private final int departamento;

    public UsuarioLogado(int id, String login, boolean adm, int departamento) {
        this.id = id;
        this.login = login;
        this.adm = adm;
        this.departamento = departamento;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public boolean isAdm() {
        return adm;
    }

    public int getDepartamento() {
        return departamento;
    }
}
