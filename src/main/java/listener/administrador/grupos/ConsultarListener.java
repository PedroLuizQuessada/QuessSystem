package listener.administrador.grupos;

import main.Main;
import view.administrador.grupos.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConsultarListener implements ActionListener {
    private final JFrame janela;
    private final int id;

    public ConsultarListener(JFrame janela, int id) {
        this.janela = janela;
        this.id = id;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        janela.dispose();
        Main.getJanelas().add(new AdicionarConsultar(id));
    }
}
