package listener.modelagem.cadastros;

import controle.DaoUtil;
import controle.validacoes.CadastroUtil;
import exception.DaoException;
import exception.validacoes.CadastroException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlterarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final CadastroUtil cadastroUtil = new CadastroUtil();

    private final Integer id;
    private final JTextField nome;
    private final JComboBox<String> tipoPermissao;
    private final JComboBox<String> permitidos;

    public AlterarListener(Integer id, JTextField nome, JComboBox<String> tipoPermissao, JComboBox<String> permitidos) {
        this.id = id;
        this.nome = nome;
        this.tipoPermissao = tipoPermissao;
        this.permitidos = permitidos;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            cadastroUtil.validarCadastro(id, nome, null);
            daoUtil.update(String.format("UPDATE CADASTROS SET nome = '%s', tipoPermissao = '%s', permitidos = %d WHERE id = %d", nome.getText(), tipoPermissao.getSelectedItem().toString(), cadastroUtil.carregaPermitidosId(tipoPermissao.getSelectedItem().toString(), permitidos.getSelectedItem().toString()), id));
            JOptionPane.showMessageDialog(null, "Cadastro alterado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (CadastroException | DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
