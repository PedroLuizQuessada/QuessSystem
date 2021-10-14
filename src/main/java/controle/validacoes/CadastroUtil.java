package controle.validacoes;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import controle.enums.TipoPermissaoEnum;
import exception.DaoException;
import exception.validacoes.CadastroException;

import javax.swing.*;
import java.util.Arrays;
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

    public String carregaPermitidosNome(String tipoPermissao, Integer permitidosId) throws DaoException{
        String permitidosNome = "Escolha os permitidos";

        if(tipoPermissao.equals(TipoPermissaoEnum.USUARIO.getDescricao())) {
            List<Map<String, Object>> usuarioList = daoUtil.select(String.format("SELECT login FROM USUARIOS WHERE id = %d", permitidosId), Collections.singletonList("login"));
            if (!usuarioList.isEmpty()) {
                permitidosNome = String.valueOf(usuarioList.get(0).get("login"));
            } else {
                permitidosNome = "Usuário não encontrado";
            }
        }
        else if(tipoPermissao.equals(TipoPermissaoEnum.DEPARTAMENTO.getDescricao())) {
            List<Map<String, Object>> departamentoList = daoUtil.select(String.format("SELECT nome FROM DEPARTAMENTOS WHERE id = %d", permitidosId), Collections.singletonList("nome"));
            if (!departamentoList.isEmpty()) {
                permitidosNome = String.valueOf(departamentoList.get(0).get("nome"));
            } else {
                permitidosNome = "Departamento não encontrado";
            }
        }
        else if(tipoPermissao.equals(TipoPermissaoEnum.GRUPO.getDescricao())) {
            List<Map<String, Object>> grupoList = daoUtil.select(String.format("SELECT nome FROM GRUPOS WHERE id = %d", permitidosId), Collections.singletonList("nome"));
            if (!grupoList.isEmpty()) {
                permitidosNome = String.valueOf(grupoList.get(0).get("nome"));
            } else {
                permitidosNome = "Grupo não encontrado";
            }
        }
        else if(tipoPermissao.equals(TipoPermissaoEnum.GERENTE.getDescricao())) {
            List<Map<String, Object>> gerenteList = daoUtil.select(String.format("SELECT d.nome AS nome, u.login AS login FROM USUARIOS u INNER JOIN DEPARTAMENTOS d ON d.id = u.departamento WHERE u.departamento = %d AND u.gerente = true", permitidosId), Arrays.asList("nome", "login"));
            if (!gerenteList.isEmpty()) {
                permitidosNome = gerenteList.get(0).get("nome") + " - " + gerenteList.get(0).get("login");
            } else {
                permitidosNome = "Gerente não encontrado";
            }
        }
        else if(tipoPermissao.equals(TipoPermissaoEnum.LIDER.getDescricao())) {
                List<Map<String, Object>> liderList = daoUtil.select(String.format("SELECT g.nome AS nome, u.login AS login FROM GRUPOS g INNER JOIN USUARIOS u ON u.id = g.lider WHERE g.id = %d", permitidosId), Arrays.asList("nome", "login"));
                if(!liderList.isEmpty()){
                    permitidosNome = liderList.get(0).get("nome") + " - " + liderList.get(0).get("login");
                }
                else {
                    permitidosNome = "Líder não encontrado";
                }
        }

        return permitidosNome;
    }

    public int carregaPermitidosId(String tipoPermissao, String permitidos) throws DaoException{
        int permitidosId = -1;

        if(tipoPermissao.equals(TipoPermissaoEnum.USUARIO.getDescricao())) {
            List<Map<String, Object>> usuarioList = daoUtil.select(String.format("SELECT id FROM USUARIOS WHERE login = '%s'", permitidos), Collections.singletonList("id"));
            if (!usuarioList.isEmpty()) {
                permitidosId = Integer.parseInt(usuarioList.get(0).get("id").toString());
            }
        }
        if(tipoPermissao.equals(TipoPermissaoEnum.DEPARTAMENTO.getDescricao())) {
            List<Map<String, Object>> departamentoList = daoUtil.select(String.format("SELECT id FROM DEPARTAMENTOS WHERE nome = '%s'", permitidos), Collections.singletonList("id"));
            if (!departamentoList.isEmpty()) {
                permitidosId = Integer.parseInt(departamentoList.get(0).get("id").toString());
            }
        }
        if(tipoPermissao.equals(TipoPermissaoEnum.GRUPO.getDescricao())) {
            List<Map<String, Object>> grupoList = daoUtil.select(String.format("SELECT id FROM GRUPOS WHERE nome = '%s'", permitidos), Collections.singletonList("id"));
            if (!grupoList.isEmpty()) {
                permitidosId = Integer.parseInt(grupoList.get(0).get("id").toString());
            }
        }
        if(tipoPermissao.equals(TipoPermissaoEnum.GERENTE.getDescricao())) {
            if (!permitidos.equalsIgnoreCase(OpcaoComboEnum.PERMITIDOS.getDescricao())) {
                permitidos = permitidos.substring(0, permitidos.indexOf(" - "));
                List<Map<String, Object>> gerenteList = daoUtil.select(String.format("SELECT d.id AS id FROM DEPARTAMENTOS d INNER JOIN USUARIOS u ON u.departamento = d.id WHERE d.nome = '%s' AND u.gerente = true", permitidos), Collections.singletonList("id"));
                if (!gerenteList.isEmpty()) {
                    permitidosId = Integer.parseInt(gerenteList.get(0).get("id").toString());
                }
            }
        }
        if(tipoPermissao.equals(TipoPermissaoEnum.LIDER.getDescricao())) {
            if (!permitidos.equalsIgnoreCase(OpcaoComboEnum.PERMITIDOS.getDescricao())) {
                permitidos = permitidos.substring(0, permitidos.indexOf(" - "));
                List<Map<String, Object>> liderList = daoUtil.select(String.format("SELECT id FROM GRUPOS WHERE nome = '%s'", permitidos), Collections.singletonList("id"));
                if (!liderList.isEmpty()) {
                    permitidosId = Integer.parseInt(liderList.get(0).get("id").toString());
                }
            }
        }
        if(tipoPermissao.equals(OpcaoComboEnum.PERMITIDOS.getDescricao())) {
                permitidosId = -1;
        }

        return permitidosId;
    }
}
