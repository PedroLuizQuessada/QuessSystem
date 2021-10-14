package listener.modelagem.cadastros;

import main.Main;
import view.modelagem.cadastros.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConsultarListener implements ActionListener {
    private final JFrame janela;
    private final Integer id;

    public ConsultarListener(JFrame janela, Integer id) {
        this.janela = janela;
        this.id = id;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        janela.dispose();
        Main.getJanelas().add(new AdicionarConsultar(id));
    }
}
