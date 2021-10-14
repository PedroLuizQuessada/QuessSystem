package listener.modelagem.cadastros.campos;

import main.Main;
import view.modelagem.cadastros.campos.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConsultarListener implements ActionListener {
    private final JFrame janela;
    private final Integer idCadastro;
    private final Integer idCampo;

    public ConsultarListener(JFrame janela, Integer idCadastro, Integer idCampo) {
        this.janela = janela;
        this.idCadastro = idCadastro;
        this.idCampo = idCampo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        janela.dispose();
        Main.getJanelas().add(new AdicionarConsultar(idCadastro, idCampo));
    }
}
