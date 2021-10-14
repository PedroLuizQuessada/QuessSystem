package listener.modelagem.cadastros;

import controle.DaoUtil;
import exception.DaoException;
import view.modelagem.cadastros.Cadastros;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExcluirListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final Cadastros janela;
    private final Integer id;
    private final String tabela;

    public ExcluirListener(Cadastros janela, Integer id, String tabela) {
        this.janela = janela;
        this.id = id;
        this.tabela = tabela;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            daoUtil.delete(String.format("DELETE FROM CADASTROS WHERE id = %d", id));
            daoUtil.drop(String.format("DROP TABLE %s", tabela));
            janela.carregarCadastros("SELECT id, nome, tabela, tipopermissao, permitidos FROM CADASTROS ORDER BY nome");
            JOptionPane.showMessageDialog(null, "Cadastro excluído", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
