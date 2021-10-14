package listener.modelagem.cadastros.campos;

import exception.DaoException;
import view.modelagem.cadastros.campos.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TipoListener implements ActionListener {
    private final JComboBox<String> tipo;
    private final List<JComponent> camposConfigs;
    private final String titulo;
    private final Integer idCadastro;
    private final Integer idCampo;
    private final AdicionarConsultar janela;

    public TipoListener(JComboBox<String> tipo, List<JComponent> camposConfigs, AdicionarConsultar janela, String titulo, Integer idCadastro, Integer idCampo) {
        this.tipo = tipo;
        this.camposConfigs = camposConfigs;
        this.titulo = titulo;
        this.idCadastro = idCadastro;
        this.idCampo = idCampo;
        this.janela = janela;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            janela.carregarConfigs(camposConfigs, tipo.getSelectedItem().toString(), titulo, idCadastro, idCampo);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
