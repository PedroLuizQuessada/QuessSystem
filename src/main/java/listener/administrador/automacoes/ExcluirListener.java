package listener.administrador.automacoes;

import controle.DaoUtil;
import exception.DaoException;
import view.administrador.automacoes.Automacoes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExcluirListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final Automacoes janela;
    private final Integer id;
    private final JCheckBox ativo;

    public ExcluirListener(Automacoes janela, Integer id, JCheckBox ativo) {
        this.janela = janela;
        this.id = id;
        this.ativo = ativo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            daoUtil.delete(String.format("DELETE FROM AUTOMACOES WHERE id = %d", id));
            janela.carregarAutomacoes(String.format("SELECT id, nome, tipopermissao, permitidos, ativo FROM AUTOMACOES WHERE ativo = %s ORDER BY nome", ativo.isSelected()));
            JOptionPane.showMessageDialog(null, "Automação excluída", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
