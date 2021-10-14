package listener.modelagem.cadastros.campos;

import controle.DaoUtil;
import exception.DaoException;
import view.modelagem.cadastros.campos.Campos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExcluirListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final Campos janela;
    private final Integer idCadastro;
    private final Integer idCampo;
    private final Integer ordemCampo;

    public ExcluirListener(Campos janela, Integer idCadastro, Integer idCampo, Integer ordemCampo) {
        this.janela = janela;
        this.idCadastro = idCadastro;
        this.idCampo = idCampo;
        this.ordemCampo = ordemCampo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordem = ordem - 1 WHERE ordem > %d AND inativo <> true", ordemCampo));
            daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET inativo = true WHERE id = %d", idCampo));
            janela.carregarCampos(String.format("SELECT id, idcadastro, ordem, label, coluna, tipo, nativo FROM CAMPOSCADASTROS WHERE idcadastro = %d AND inativo <> true ORDER BY ordem", idCadastro));
            JOptionPane.showMessageDialog(null, "Campo excluído", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
