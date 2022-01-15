package listener.cadastro;

import main.Main;
import view.cadastro.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdicionarListener implements ActionListener {
    private final JFrame janela;
    private final Integer idCadastro;
    private final String nomeCadastro;
    private final Integer idRegistro;

    public AdicionarListener(JFrame janela, Integer idCadastro, String nomeCadastro, Integer idRegistro) {
        this.janela = janela;
        this.idCadastro = idCadastro;
        this.nomeCadastro = nomeCadastro;
        this.idRegistro = idRegistro;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        janela.dispose();
        Main.getJanelas().add(new AdicionarConsultar(idCadastro, nomeCadastro, idRegistro));
    }
}
