package listener.modelagem.cadastros.campos;

import controle.enums.AcoesCampoCadastroEnum;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AcoesListener implements ActionListener {
    private final JComboBox<String> acoes;
    private final JButton realizarAcao;

    public AcoesListener(JComboBox<String> acoes, JButton realizarAcao) {
        this.acoes = acoes;
        this.realizarAcao = realizarAcao;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(acoes.getSelectedItem() != null) {
            realizarAcao.setEnabled(!acoes.getSelectedItem().toString().equals(AcoesCampoCadastroEnum.PADRAO.getDescricao()));

            realizarAcao.setText(acoes.getSelectedItem().toString());
        }
    }
}
