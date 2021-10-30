package controle;

import controle.enums.AcoesCampoCadastroEnum;
import controle.enums.OpcaoComboEnum;
import controle.enums.TipoCampoEnum;
import controle.enums.TipoPermissaoEnum;
import exception.DaoException;

import javax.swing.*;
import java.util.*;

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

    public void carregarAcoesCampo(JComboBox<String> comboBox, int idCampo, boolean cadastro) throws DaoException{
        comboBox.removeAllItems();
        comboBox.addItem(AcoesCampoCadastroEnum.PADRAO.getDescricao());
        comboBox.setSelectedItem(AcoesCampoCadastroEnum.PADRAO.getDescricao());

        List<Map<String, Object>> infosCampoList = new ArrayList<>();

        if(cadastro) {
            infosCampoList = daoUtil.select(String.format("SELECT vinculado, bloqueado, obrigatorio, pesquisavel, quebralinha, coluna, tipo, agrupador FROM CAMPOSCADASTROS WHERE id = %d", idCampo), Arrays.asList("vinculado", "bloqueado", "obrigatorio", "pesquisavel", "quebralinha", "coluna", "tipo", "agrupador"));
        }

        if(infosCampoList.get(0).get("vinculado") != null && infosCampoList.get(0).get("vinculado").toString().equalsIgnoreCase("true")){
            comboBox.addItem(AcoesCampoCadastroEnum.DESVINCULAR.getDescricao());
        }
        else {
            comboBox.addItem(AcoesCampoCadastroEnum.VINCULAR.getDescricao());
        }
        if(infosCampoList.get(0).get("bloqueado") != null && infosCampoList.get(0).get("bloqueado").toString().equalsIgnoreCase("true")){
            comboBox.addItem(AcoesCampoCadastroEnum.DESBLOQUEAR.getDescricao());
        }
        else {
            comboBox.addItem(AcoesCampoCadastroEnum.BLOQUEAR.getDescricao());
        }
        if(infosCampoList.get(0).get("obrigatorio") != null && infosCampoList.get(0).get("obrigatorio").toString().equalsIgnoreCase("true")){
            comboBox.addItem(AcoesCampoCadastroEnum.TORNAR_NAO_OBRIGATORIO.getDescricao());
        }
        else {
            comboBox.addItem(AcoesCampoCadastroEnum.TORNAR_OBRIGATORIO.getDescricao());
        }
        if(infosCampoList.get(0).get("pesquisavel") != null && infosCampoList.get(0).get("pesquisavel").toString().equalsIgnoreCase("true") && !infosCampoList.get(0).get("coluna").toString().equalsIgnoreCase("id")){
            comboBox.addItem(AcoesCampoCadastroEnum.TORNAR_NAO_PESQUISAVEL.getDescricao());
        }
        else if (infosCampoList.get(0).get("pesquisavel") != null &&
                !infosCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.AGRUPADOR.getDescricao()) &&
                !infosCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.AREATEXTO.getDescricao()) &&
                infosCampoList.get(0).get("agrupador") == null &&
                !infosCampoList.get(0).get("coluna").toString().equalsIgnoreCase("id")){
            comboBox.addItem(AcoesCampoCadastroEnum.TORNAR_PESQUISAVEL.getDescricao());
        }
        if(infosCampoList.get(0).get("quebralinha") != null && infosCampoList.get(0).get("quebralinha").toString().equalsIgnoreCase("true")){
            comboBox.addItem(AcoesCampoCadastroEnum.NAO_QUEBRAR_LINHA.getDescricao());
        }
        else {
            comboBox.addItem(AcoesCampoCadastroEnum.QUEBRAR_LINHA.getDescricao());
        }
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

    public void carregarInfosCombobox(Integer idCampo, JComboBox<String> combobox) throws DaoException {
        combobox.removeAllItems();

        combobox.addItem(OpcaoComboEnum.OPCAO_COMBO_RADIO.getDescricao());
        combobox.setSelectedItem(OpcaoComboEnum.OPCAO_COMBO_RADIO.getDescricao());

        List<Map<String, Object>> opcoesList = daoUtil.select(String.format("SELECT opcoes FROM CONFIGSCAMPOSCOMBOBOX WHERE idcampo = %d", idCampo), Collections.singletonList("opcoes"));

        if(opcoesList.get(0).get("opcoes") != null) {
            String[] opcoes = opcoesList.get(0).get("opcoes").toString().split("_");

            for (String opcao: opcoes){
                combobox.addItem(opcao);
            }
        }
    }
}
