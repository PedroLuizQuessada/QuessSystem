package controle.validacoes;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;
import exception.validacoes.OpcaoException;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OpcaoUtil {
    private final DaoUtil daoUtil = new DaoUtil();

    public void validarOpcao(Integer idCampo, boolean cadastro, JTextField novaOpcao, String tabela) throws DaoException, OpcaoException{
        String novaOpcaoTexto = novaOpcao.getText();
        novaOpcao.setText("");

        if(novaOpcaoTexto.contains("_")){
            throw new OpcaoException("Opção não pode conter \"_\"");
        }
        if(novaOpcaoTexto.length() == 0){
            throw new OpcaoException("Opção não pode ser vazia");
        }
        if(novaOpcaoTexto.equals(OpcaoComboEnum.OPCAO_COMBO_RADIO.getDescricao()) || novaOpcaoTexto.equals(OpcaoComboEnum.SEM_OPCAO_PADRAO.getDescricao())){
            throw new OpcaoException("Opção inválida");
        }

        List<Map<String, Object>> opcoesList = daoUtil.select(String.format("SELECT opcoes FROM %s WHERE idcampo = %d AND cadastro = %s", tabela, idCampo, cadastro), Collections.singletonList("opcoes"));
        if(opcoesList.get(0).get("opcoes") != null) {
            String[] opcoes = opcoesList.get(0).get("opcoes").toString().split("_");

            for(String opcao: opcoes){
                if(novaOpcaoTexto.equals(opcao)){
                    throw new OpcaoException("Opção já havia sido adicionada");
                }
            }
        }
    }
}
