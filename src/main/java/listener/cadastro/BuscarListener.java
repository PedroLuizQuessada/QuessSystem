package listener.cadastro;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import controle.enums.TipoCampoEnum;
import controle.mascaras.DataHoraUtil;
import controle.mascaras.DataUtil;
import exception.DaoException;
import view.cadastro.Cadastro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BuscarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final Cadastro janela;
    private final List<Map<String, JComponent>> camposPesquisaveis;
    private final int idCadastro;

    public BuscarListener(Cadastro janela, List<Map<String, JComponent>> camposPesquisaveis, int idCadastro) {
        this.janela = janela;
        this.camposPesquisaveis = camposPesquisaveis;
        this.idCadastro = idCadastro;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String where = " WHERE 1 = 1 ";

            for (Map<String, JComponent> campoPesquisavel : camposPesquisaveis) {
                for (Map.Entry<String, JComponent> valores : campoPesquisavel.entrySet()) {
                    if (valores.getValue().getClass().equals(JTextField.class)) {
                        JTextField filtro = (JTextField) valores.getValue();
                        if (!filtro.getText().equalsIgnoreCase("")) {
                            List<Map<String, Object>> tipoCampoLista = daoUtil.select(String.format("SELECT tipo FROM CAMPOSCADASTROS WHERE idcadastro = %d AND coluna = '%s'", idCadastro, valores.getKey()), Collections.singletonList("tipo"));
                            String tipoCampo = tipoCampoLista.get(0).get("tipo").toString();
                            if(tipoCampo.equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())) {
                                where = where + String.format(" AND %s = %s ", valores.getKey(), filtro.getText());
                            }
                            else if(tipoCampo.equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())){
                                DataUtil dataUtil = new DataUtil(filtro);
                                String dataFormatada = dataUtil.converterData();

                                where = where + String.format(" AND %s = '%s' ", valores.getKey(), dataFormatada);
                            }
                            else if(tipoCampo.equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())){
                                DataHoraUtil dataHoraUtil = new DataHoraUtil(filtro);
                                String dataHoraFormatada = dataHoraUtil.converterDataHora();

                                where = where + String.format(" AND %s = '%s' ", valores.getKey(), dataHoraFormatada);
                            }
                            else {
                                where = where + String.format(" AND %s LIKE '%s' ", valores.getKey(), filtro.getText());
                            }
                        }
                    }
                    else if (valores.getValue().getClass().equals(JComboBox.class)) {
                        JComboBox<String> filtro = (JComboBox<String>) valores.getValue();
                        if (!filtro.getSelectedItem().toString().equalsIgnoreCase(OpcaoComboEnum.OPCAO_COMBO_RADIO.getDescricao())) {
                            where = where + String.format(" AND %s = '%s' ", valores.getKey(), filtro.getSelectedItem().toString());
                        }
                    }
                    else if (valores.getValue().getClass().equals(JCheckBox.class)) {
                        JCheckBox filtro = (JCheckBox) valores.getValue();
                        where = where + String.format(" AND %s = %s", valores.getKey(), filtro.isSelected());
                    }
                }
            }

            where = where + " order BY id";
            janela.carregarRegistros(String.format("SELECT * FROM %s %s", janela.getNomeCadastro(), where));
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
