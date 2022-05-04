package listener.modelagem.cadastros.campos;

import main.Main;
import view.modelagem.RegrasCondicionais;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegrasCondicionaisListener implements ActionListener {
    private final JFrame janela;
    private final Integer idCampo;
    private final Integer idFormulario;
    private final boolean cadastro;

    public RegrasCondicionaisListener(JFrame janela, Integer idCampo, Integer idFormulario, boolean cadastro) {
        this.janela = janela;
        this.idCampo = idCampo;
        this.idFormulario = idFormulario;
        this.cadastro = cadastro;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        janela.dispose();
        Main.getJanelas().add(new RegrasCondicionais(idCampo, idFormulario, cadastro));
    }
}
