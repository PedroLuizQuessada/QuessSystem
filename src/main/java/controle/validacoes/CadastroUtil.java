package controle.validacoes;

import controle.DaoUtil;
import exception.DaoException;
import exception.validacoes.CadastroException;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CadastroUtil {
    private final DaoUtil daoUtil = new DaoUtil();

    public void validarCadastro(Integer id, JTextField nome, String tabela) throws CadastroException, DaoException {
        if(nome.getText().length() < 1){
            throw new CadastroException("O cadastro deve ter um nome");
        }

        if(tabela != null && tabela.length() < 1){
            throw new CadastroException("A tabela do cadastro deve ter um nome");
        }

        List<Map<String, Object>> cadastros = daoUtil.select(String.format("SELECT COUNT(id) AS total FROM CADASTROS WHERE (nome = '%s' OR tabela = '%s') AND id <> %d", nome.getText(), tabela, id), Collections.singletonList("total"));
        if(Integer.parseInt(cadastros.get(0).get("total").toString()) > 0){
            throw new CadastroException("O nome do cadastro e da tabela devem ser únicos");
        }
    }
}
