package listener.home;

import main.Main;
import view.administrador.automacoes.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutomacaoListener implements ActionListener {
    private final JFrame janela;
    private final Integer idAutomacao;

    public AutomacaoListener(JFrame janela, Integer idAutomacao) {
        this.janela = janela;
        this.idAutomacao = idAutomacao;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        janela.dispose();
        Main.getJanelas().add(new AdicionarConsultar(idAutomacao));
    }
}
