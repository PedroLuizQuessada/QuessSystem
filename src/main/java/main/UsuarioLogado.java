package main;

public class UsuarioLogado {
    private final String login;
    private final boolean adm;

    public UsuarioLogado(String login, boolean adm) {
        this.login = login;
        this.adm = adm;
    }

    public String getLogin() {
        return login;
    }

    public boolean isAdm() {
        return adm;
    }
}
