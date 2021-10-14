package controle.validacoes;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;
import exception.validacoes.GrupoException;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GrupoUtil {
    private final DaoUtil daoUtil = new DaoUtil();

    public void validarGrupo(Integer id, JTextField nome) throws GrupoException, DaoException {
        if(nome.getText().length() < 1){
            throw new GrupoException("O grupo deve ter um nome");
        }

        if(nome.getText().equalsIgnoreCase(OpcaoComboEnum.GRUPO.getDescricao()) || nome.getText().equalsIgnoreCase(OpcaoComboEnum.PERMITIDOS.getDescricao()) || nome.getText().contains(" - ")){
            throw new GrupoException("Nome reservado para o sistema");
        }

        List<Map<String, Object>> grupos = daoUtil.select(String.format("SELECT COUNT(id) as total FROM GRUPOS WHERE nome = '%s' AND id <> %d", nome.getText(), id), Collections.singletonList("total"));
        if(Integer.parseInt(grupos.get(0).get("total").toString()) > 0){
            throw new GrupoException("O nome deve ser único");
        }
    }
}
