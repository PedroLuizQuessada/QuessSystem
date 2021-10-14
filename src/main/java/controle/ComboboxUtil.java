package controle;

import controle.enums.TipoCampoEnum;
import controle.enums.TipoPermissaoEnum;
import exception.DaoException;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ComboboxUtil {
    private final DaoUtil daoUtil = new DaoUtil();

    public void carregarTiposPermissao(JComboBox<String> comboBox){
        for (TipoPermissaoEnum tipoPermissaoEnum : TipoPermissaoEnum.values()){
            comboBox.addItem(tipoPermissaoEnum.getDescricao());
        }

        comboBox.setSelectedItem(TipoPermissaoEnum.PADRAO.getDescricao());
    }

    public void carregarTiposCampos(JComboBox<String> comboBox){
        for (TipoCampoEnum tipoCampoEnum: TipoCampoEnum.values()){
            comboBox.addItem(tipoCampoEnum.getDescricao());
        }

        comboBox.setSelectedItem(TipoCampoEnum.PADRAO.getDescricao());
    }

    public void carregarOrdem(Integer idCadastro, JComboBox<String> ordemCombobox, boolean novoCampo) throws DaoException {
        List<Map<String, Object>> ordemList = daoUtil.select(String.format("SELECT MAX(ordem) AS ordem FROM CAMPOSCADASTROS WHERE idcadastro = %d AND inativo <> true", idCadastro), Collections.singletonList("ordem"));
        int ordem = Integer.parseInt(ordemList.get(0).get("ordem").toString());

        int soma = 0;
        if(novoCampo){
            soma = 1;
        }
        for(int i = 1; i <= ordem + soma; i++){
            ordemCombobox.addItem(String.valueOf(i));
        }

        ordemCombobox.setSelectedItem(String.valueOf(ordem + soma));
    }
}
