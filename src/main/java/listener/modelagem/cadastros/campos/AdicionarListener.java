package listener.modelagem.cadastros.campos;

import main.Main;
import view.modelagem.cadastros.campos.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdicionarListener implements ActionListener {
    private final JFrame janela;
    private final Integer idCadastro;

    public AdicionarListener(JFrame janela, Integer idCadastro) {
        this.janela = janela;
        this.idCadastro = idCadastro;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        janela.dispose();
        Main.getJanelas().add(new AdicionarConsultar(idCadastro, null));
    }
}
