package main;

public class UsuarioLogado {
    private final int id;
    private final String login;
    private final String senha;
    private final String email;
    private final boolean adm;
    private final int departamento;

    public UsuarioLogado(int id, String login, String senha, String email, boolean adm, int departamento) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.adm = adm;
        this.departamento = departamento;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAdm() {
        return adm;
    }

    public int getDepartamento() {
        return departamento;
    }
}
