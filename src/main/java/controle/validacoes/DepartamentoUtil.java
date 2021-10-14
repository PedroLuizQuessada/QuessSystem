package controle.validacoes;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;
import exception.validacoes.DepartamentoException;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DepartamentoUtil {
    private final DaoUtil daoUtil = new DaoUtil();

    public void validarDepartamento(Integer id, JTextField nome) throws DepartamentoException, DaoException {
        if(nome.getText().length() < 1){
            throw new DepartamentoException("O departamento deve ter um nome");
        }

        if(nome.getText().equalsIgnoreCase(OpcaoComboEnum.DEPARTAMENTO.getDescricao()) || nome.getText().equalsIgnoreCase(OpcaoComboEnum.PERMITIDOS.getDescricao()) || nome.getText().contains(" - ")){
            throw new DepartamentoException("Nome reservado para o sistema");
        }

        List<Map<String, Object>> departamentos = daoUtil.select(String.format("SELECT COUNT(id) as total FROM DEPARTAMENTOS WHERE nome = '%s' AND id <> %d", nome.getText(), id), Collections.singletonList("total"));
        if(Integer.parseInt(departamentos.get(0).get("total").toString()) > 0){
            throw new DepartamentoException("O nome deve ser único");
        }
    }
}
