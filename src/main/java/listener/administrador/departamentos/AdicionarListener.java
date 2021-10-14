package listener.administrador.departamentos;

import main.Main;
import view.administrador.departamentos.AdicionarConsultar;

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
