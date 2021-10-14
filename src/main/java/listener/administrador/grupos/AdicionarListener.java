package listener.administrador.grupos;

import main.Main;
import view.administrador.grupos.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdicionarListener implements ActionListener {
    private final JFrame janela;

    public AdicionarListener(JFrame janela) {
        this.janela = janela;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        janela.dispose();
        Main.getJanelas().add(new AdicionarConsultar(null));
    }
}
