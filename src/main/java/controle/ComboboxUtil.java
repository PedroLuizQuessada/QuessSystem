package controle;

import controle.enums.TipoCampoEnum;
import controle.enums.TipoPermissaoEnum;
import exception.DaoException;

import javax.swing.*;
import java.util.ArrayList;
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

    public void carregarOrdem(Integer idFormulario, JComboBox<String> ordemCombobox, boolean novoCampo, boolean cadastro) throws DaoException {
        ordemCombobox.removeAllItems();
        List<Map<String, Object>> ordemList = new ArrayList<>();
        if (cadastro) {
            ordemList = daoUtil.select(String.format("SELECT MAX(ordem) AS ordem FROM CAMPOSCADASTROS WHERE idcadastro = %d AND inativo <> true", idFormulario), Collections.singletonList("ordem"));
        }
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

    public void carregarOrdemAgrupador(Integer idFormulario, JComboBox<String> ordemCombobox, boolean novoCampo, boolean cadastro, String agrupadorLabel) throws DaoException {
        ordemCombobox.removeAllItems();
        List<Map<String, Object>> ordemList = new ArrayList<>();
        if (cadastro) {
            List<Map<String, Object>> agrupadorIdList = daoUtil.select(String.format("SELECT id FROM CAMPOSCADASTROS WHERE label = '%s' AND idcadastro = %d", agrupadorLabel, idFormulario), Collections.singletonList("id"));
            ordemList = daoUtil.select(String.format("SELECT MAX(ordemagrupador) AS ordemagrupador FROM CAMPOSCADASTROS WHERE idcadastro = %d AND agrupador = %d AND inativo <> true", idFormulario, Integer.parseInt(agrupadorIdList.get(0).get("id").toString())), Collections.singletonList("ordemagrupador"));
        }

        int ordem = 0;
        if(ordemList.get(0).get("ordemagrupador") != null) {
            ordem = Integer.parseInt(ordemList.get(0).get("ordemagrupador").toString());
        }

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
