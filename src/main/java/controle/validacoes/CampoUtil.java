package controle.validacoes;

import controle.DaoUtil;
import controle.enums.TipoCampoEnum;
import exception.DaoException;
import exception.validacoes.CampoException;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CampoUtil {
    private final DaoUtil daoUtil = new DaoUtil();

    public void validarCampo(Integer idCadastro, Integer idCampo, JTextField label, String coluna, JComboBox<String> tipo) throws CampoException, DaoException {
        if(label.getText().length() < 1 || (coluna != null && coluna.length() < 1)){
            throw new CampoException("O campo deve ter um label e um nome para coluna");
        }

        if(tipo != null && tipo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.PADRAO.getDescricao())){
            throw new CampoException("Necessário escolher um tipo para o campo");
        }

        List<Map<String, Object>> campos = daoUtil.select(String.format("SELECT COUNT(id) AS total FROM CAMPOSCADASTROS WHERE idcadastro = %d AND (label = '%s' OR coluna = '%s') AND id <> %d", idCadastro, label.getText(), coluna, idCampo), Collections.singletonList("total"));
        if(Integer.parseInt(campos.get(0).get("total").toString()) > 0){
            throw new CampoException("O label e o nome da coluna do campo devem ser únicos");
        }
    }
}
