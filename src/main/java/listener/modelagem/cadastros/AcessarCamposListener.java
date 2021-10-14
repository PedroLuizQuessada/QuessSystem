package listener.modelagem.cadastros;

import main.Main;
import view.modelagem.cadastros.AdicionarConsultar;
import view.modelagem.cadastros.campos.Campos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AcessarCamposListener implements ActionListener {
    private final AdicionarConsultar janela;
    private final Integer id;

    public AcessarCamposListener(AdicionarConsultar janela, Integer id) {
        this.janela = janela;
        this.id = id;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        janela.dispose();
        Main.getJanelas().add(new Campos(id));
    }
}
