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
    private final Integer agrupadorId;
    private final boolean agrupador;

    public ExcluirListener(Campos janela, Integer idCadastro, Integer idCampo, Integer ordemCampo, Integer agrupadorId, boolean agrupador) {
        this.janela = janela;
        this.idCadastro = idCadastro;
        this.idCampo = idCampo;
        this.ordemCampo = ordemCampo;
        this.agrupadorId = agrupadorId;
        this.agrupador = agrupador;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if(agrupadorId == null) {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordem = ordem - 1 WHERE ordem > %d AND inativo <> true AND idcadastro = %d", ordemCampo, idCadastro));
            }
            else {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordemagrupador = ordemagrupador - 1 WHERE ordemagrupador > %d AND agrupador = %d AND inativo <> true AND idcadastro = %d", ordemCampo, agrupadorId, idCadastro));
            }

            if (agrupador){
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET inativo = true WHERE idcadastro = %d AND agrupador = %d", idCadastro, idCampo));
            }

            daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET inativo = true WHERE id = %d", idCampo));
            janela.carregarCampos(String.format("SELECT id, idcadastro, ordem, label, coluna, tipo, nativo, agrupador, ordemagrupador FROM CAMPOSCADASTROS WHERE idcadastro = %d AND inativo <> true ORDER BY ordem, ordemagrupador", idCadastro));
            JOptionPane.showMessageDialog(null, "Campo excluído", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception){
            exception.printStackTrace();
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
