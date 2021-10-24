package listener.home;

import main.Main;
import view.cadastro.Cadastro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CadastroListener implements ActionListener {
    private final JFrame janela;
    private final int idCadastro;

    public CadastroListener(JFrame janela, int idCadastro) {
        this.janela = janela;
        this.idCadastro = idCadastro;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        janela.dispose();
        Main.getJanelas().add(new Cadastro(idCadastro));
    }
}
