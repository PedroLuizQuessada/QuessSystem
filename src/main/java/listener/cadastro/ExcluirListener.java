package listener.cadastro;

import controle.DaoUtil;
import controle.enums.TipoCampoEnum;
import exception.DaoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExcluirListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final int idCadastro;
    private final String nomeCadastro;
    private final int idRegistro;

    public ExcluirListener(int idCadastro, String nomeCadastro, int idRegistro) {
        this.idCadastro = idCadastro;
        this.nomeCadastro = nomeCadastro;
        this.idRegistro = idRegistro;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            daoUtil.delete(String.format("DELETE FROM %s WHERE id = %d", nomeCadastro, idRegistro));

            List<Map<String, Object>> agrupadores = daoUtil.select(String.format("SELECT id FROM CAMPOSCADASTROS WHERE tipo = '%s' AND idcadastro = %d", TipoCampoEnum.AGRUPADOR.getDescricao(), idCadastro), Collections.singletonList("id"));

            for (Map<String, Object> agrupador: agrupadores){
                daoUtil.delete(String.format("DELETE FROM agrupador_cad_%d WHERE id_rel = %d", Integer.valueOf(agrupador.get("id").toString()), idRegistro));
            }
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
