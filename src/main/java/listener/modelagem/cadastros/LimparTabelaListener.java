package listener.modelagem.cadastros;

import controle.DaoUtil;
import exception.DaoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LimparTabelaListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final JTextField tabela;

    public LimparTabelaListener(JTextField tabela) {
        this.tabela = tabela;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            daoUtil.delete(String.format("DELETE FROM %s", tabela.getText()));
            JOptionPane.showMessageDialog(null, "Cadastro limpo", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
