package listener.home;

import main.Main;
import view.Home;
import view.administrador.automacoes.Automacoes;
import view.administrador.departamentos.AdicionarConsultar;
import view.administrador.departamentos.Departamentos;
import view.administrador.grupos.Grupos;
import view.administrador.usuarios.Usuarios;
import view.cadastro.Cadastro;
import view.modelagem.cadastros.Cadastros;
import view.modelagem.cadastros.campos.Campos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VoltarListener implements ActionListener {
    private final JFrame janela;
    private final Integer id;

    public VoltarListener(JFrame janela, Integer... id) {
        this.janela = janela;
        this.id = id.length > 0 ? id[0] : 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        janela.dispose();

        switch (janela.getClass().getName()){
            case "view.AtualizarCredenciais":

            case "view.administrador.departamentos.Departamentos":

            case "view.administrador.grupos.Grupos":

            case "view.administrador.usuarios.Usuarios":

            case "view.administrador.automacoes.Automacoes":

            case "view.modelagem.cadastros.Cadastros":

            case "view.cadastro.Cadastro":
                Main.getJanelas().add(new Home());
                break;

            case "view.administrador.departamentos.AdicionarConsultar":
                Main.getJanelas().add(new Departamentos());
                break;

            case "view.administrador.grupos.AdicionarConsultar":
                Main.getJanelas().add(new Grupos());
                break;

            case "view.administrador.usuarios.AdicionarConsultar":
                switch (Main.getJanelas().get(Main.getJanelas().size() - 2).getClass().getName()){
                    case "view.administrador.usuarios.Usuarios":
                        Main.getJanelas().add(new Usuarios());
                        break;

                    case "view.administrador.departamentos.AdicionarConsultar":
                        Main.getJanelas().add(new AdicionarConsultar(id));
                        break;
                }
                break;

            case "view.modelagem.cadastros.AdicionarConsultar":
                Main.getJanelas().add(new Cadastros());
                break;

            case "view.modelagem.cadastros.campos.Campos":
                Main.getJanelas().add(new view.modelagem.cadastros.AdicionarConsultar(id));
                break;

            case "view.modelagem.cadastros.campos.AdicionarConsultar":

            case "view.modelagem.RegrasCondicionais":
                Main.getJanelas().add(new Campos(id));
                break;

            case "view.cadastro.AdicionarConsultar":
                Main.getJanelas().add(new Cadastro(id));
                break;

            case "view.administrador.automacoes.AdicionarConsultar":
                switch (Main.getJanelas().get(Main.getJanelas().size() - 2).getClass().getName()){
                    case "view.administrador.automacoes.Automacoes":
                        Main.getJanelas().add(new Automacoes());
                        break;

                    case "src.main.java.view.Home":
                        Main.getJanelas().add(new Home());
                        break;
                }
                break;
        }
    }
}
