package listener.cadastro;

import main.Main;
import view.cadastro.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class RecarregarFormularioListener implements ActionListener {
    private final AdicionarConsultar janela;
    private final Integer idCadastro;
    private final String nomeCadastro;
    private final Integer idRegistro;
    private final Map<String, Object> campos;
    private final Map<String, Object> valoresCampos = new HashMap<>();

    public RecarregarFormularioListener(AdicionarConsultar janela, Integer idCadastro, String nomeCadastro, Integer idRegistro, Map<String, Object> campos) {
        this.janela = janela;
        this.idCadastro = idCadastro;
        this.nomeCadastro = nomeCadastro;
        this.idRegistro = idRegistro;
        this.campos = campos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Map.Entry<String, Object> campo: campos.entrySet()) {
            if (campo.getValue().getClass().equals(JTextField.class)) {
                valoresCampos.put(campo.getKey(), ((JTextField) campo.getValue()).getText());
            }
            else if (campo.getValue().getClass().equals(JCheckBox.class)) {
                valoresCampos.put(campo.getKey(), ((JCheckBox) campo.getValue()).isSelected());
            }
            else if (campo.getValue().getClass().equals(JTextArea.class)) {
                valoresCampos.put(campo.getKey(), ((JTextField) campo.getValue()).getText());
            }
            else if (campo.getValue().getClass().equals(JComboBox.class)) {
                valoresCampos.put(campo.getKey(), ((JComboBox<String>) campo.getValue()).getSelectedItem());
            }
            else if (campo.getValue().getClass().equals(ButtonGroup.class)) {
                valoresCampos.put(campo.getKey(), ((ButtonGroup) campo.getValue()).getSelection().getActionCommand());
            }
        }

        janela.dispose();
        Main.getJanelas().add(new AdicionarConsultar(idCadastro, nomeCadastro, idRegistro, valoresCampos));
    }
}
