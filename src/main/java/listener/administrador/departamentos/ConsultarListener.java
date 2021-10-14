package listener.administrador.departamentos;

import main.Main;
import view.administrador.departamentos.AdicionarConsultar;

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
