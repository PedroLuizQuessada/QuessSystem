package listener.modelagem;

import controle.ComboboxUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AgrupadorOrdemListener implements ActionListener {
    private final ComboboxUtil comboboxUtil = new ComboboxUtil();

    private final JComboBox<String> agrupador;
    private final JComboBox<String> ordem;
    private final boolean cadastro;
    private final int idFormulario;

    public AgrupadorOrdemListener(JComboBox<String> agrupador, JComboBox<String> ordem, boolean cadastro, int idFormulario) {
        this.agrupador = agrupador;
        this.ordem = ordem;
        this.cadastro = cadastro;
        this.idFormulario = idFormulario;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ordem.removeAllItems();

        try {
            if (!agrupador.getSelectedItem().toString().equalsIgnoreCase(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao())) {
                String agrupadorLabel = agrupador.getSelectedItem().toString();
                comboboxUtil.carregarOrdemAgrupador(idFormulario, ordem, true, cadastro, agrupadorLabel);
            } else {
                comboboxUtil.carregarOrdem(idFormulario, ordem, true, cadastro);
            }
        } catch (DaoException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
