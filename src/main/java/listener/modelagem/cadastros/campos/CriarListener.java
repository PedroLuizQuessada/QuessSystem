package listener.modelagem.cadastros.campos;

import controle.ConfigsUtil;
import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import controle.enums.TipoCampoEnum;
import controle.validacoes.CampoUtil;
import exception.DaoException;
import exception.validacoes.CampoException;
import main.Main;
import view.modelagem.cadastros.campos.AdicionarConsultar;
import view.modelagem.cadastros.campos.Campos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class CriarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final CampoUtil campoUtil = new CampoUtil();
    private final ConfigsUtil configsUtil;

    private final AdicionarConsultar janela;
    private final JComboBox<String> ordem;
    private final JTextField label;
    private final JTextField coluna;
    private final JComboBox<String> tipo;
    private final Integer idCampo;
    private final Integer idCadastro;

    public CriarListener(AdicionarConsultar janela, Integer idCadastro, Integer idCampo, ConfigsUtil configsUtil) {
        this.configsUtil = configsUtil;
        this.janela = janela;
        this.ordem = janela.getOrdem();
        this.label = janela.getLabel();
        this.coluna = janela.getColuna();
        this.tipo = janela.getTipo();
        this.idCampo = idCampo;
        this.idCadastro = idCadastro;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            campoUtil.validarCampo(idCadastro, idCampo, label, coluna.getText(), tipo, configsUtil);

            List<Map<String, Object>> tabelaCadastroList = daoUtil.select(String.format("SELECT tabela FROM CADASTROS WHERE id = %d", idCadastro), Collections.singletonList("tabela"));
            String tabelaCadastro = tabelaCadastroList.get(0).get("tabela").toString();

            StringBuilder sqlCreateTable = new StringBuilder(String.format("CREATE TABLE %s " +
                    "(id INTEGER NOT NULL IDENTITY, ", tabelaCadastro));
            StringBuilder sqlInsert = new StringBuilder(String.format("INSERT INTO %s (", tabelaCadastro));
            List<String> colunasList = new ArrayList<>();

            if(configsUtil.getAgrupador() != null && !configsUtil.getAgrupador().getSelectedItem().toString().equals(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao())){
                List<Map<String, Object>> agrList = daoUtil.select(String.format("SELECT id, ordem FROM CAMPOSCADASTROS WHERE label = '%s' AND idcadastro = %d", configsUtil.getAgrupador().getSelectedItem(), idCadastro), Arrays.asList("id", "ordem"));
                Integer idAgr = Integer.parseInt(agrList.get(0).get("id").toString());
                Integer ordemAgr = Integer.parseInt(agrList.get(0).get("ordem").toString());

                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordemagrupador = ordemagrupador + 1 WHERE ordemagrupador >= %d AND agrupador = %d", Integer.parseInt(ordem.getSelectedItem().toString()), idAgr));
                daoUtil.insert(String.format("INSERT INTO CAMPOSCADASTROS (idcadastro, ordem, ordemagrupador, label, coluna, tipo, vinculado, bloqueado, obrigatorio, pesquisavel, agrupador) VALUES (%d, %d, %d, '%s', '%s', '%s', false, false, false, false, %d)", idCadastro, ordemAgr, Integer.parseInt(ordem.getSelectedItem().toString()), label.getText(), coluna.getText(), tipo.getSelectedItem(), idAgr));
            }
            else {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordem = ordem + 1 WHERE ordem >= %d", Integer.parseInt(ordem.getSelectedItem().toString())));
                daoUtil.insert(String.format("INSERT INTO CAMPOSCADASTROS (idcadastro, ordem, label, coluna, tipo, vinculado, bloqueado, obrigatorio, pesquisavel, ordemagrupador) VALUES (%d, %d, '%s', '%s', '%s', false, false, false, false, 0)", idCadastro, Integer.parseInt(ordem.getSelectedItem().toString()), label.getText(), coluna.getText(), tipo.getSelectedItem()));
            }

            List<Map<String, Object>> colunas = daoUtil.select(String.format("SELECT coluna, tipo FROM CAMPOSCADASTROS WHERE idcadastro = %d AND coluna != 'id'", idCadastro), Arrays.asList("coluna", "tipo"));
            for(Map<String, Object> coluna: colunas){
                if (String.valueOf(coluna.get("tipo")).equalsIgnoreCase(TipoCampoEnum.AGRUPADOR.getDescricao())){
                    break;
                }

                sqlCreateTable.append(coluna.get("coluna"));

                if(String.valueOf(coluna.get("tipo")).equalsIgnoreCase(TipoCampoEnum.TEXTO.getDescricao())) {
                    sqlCreateTable.append(" VARCHAR(30), ");
                }
                else if(String.valueOf(coluna.get("tipo")).equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())) {
                    sqlCreateTable.append(" INTEGER, ");
                }
                else if (String.valueOf(coluna.get("tipo")).equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())){
                    sqlCreateTable.append(" TIMESTAMP, ");
                }
                else if (String.valueOf(coluna.get("tipo")).equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())){
                    sqlCreateTable.append(" DATE, ");
                }
                else if (String.valueOf(coluna.get("tipo")).equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())){
                    sqlCreateTable.append(" BIT, ");
                }
                else if (String.valueOf(coluna.get("tipo")).equalsIgnoreCase(TipoCampoEnum.AREATEXTO.getDescricao())){
                    sqlCreateTable.append(" VARCHAR(500), ");
                }
                else if (String.valueOf(coluna.get("tipo")).equalsIgnoreCase(TipoCampoEnum.COMBOBOX.getDescricao())){
                    sqlCreateTable.append(" VARCHAR(30), ");
                }
                else if (String.valueOf(coluna.get("tipo")).equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())){
                    sqlCreateTable.append(" VARCHAR(30), ");
                }

                sqlInsert.append(coluna.get("coluna").toString()).append(", ");
                colunasList.add(coluna.get("coluna").toString());
            }
            sqlCreateTable.append(" PRIMARY KEY(id))");
            sqlInsert = new StringBuilder(sqlInsert.substring(0, sqlInsert.length() - 2) + ") VALUES (");

            colunasList.remove(coluna.getText());
            List<Map<String, Object>> infosTabela = daoUtil.select(String.format("SELECT * FROM %s", tabelaCadastro), colunasList);

            daoUtil.drop(String.format("DROP TABLE %s", tabelaCadastro));
            daoUtil.create(sqlCreateTable.toString());

            for(Map<String, Object> infoTabela: infosTabela) {
                StringBuilder sqlInsertLinha = new StringBuilder(sqlInsert.toString());
                for (Map<String, Object> coluna : colunas) {
                    if (!coluna.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.AGRUPADOR.getDescricao()) && !coluna.get("coluna").equals(this.coluna.getText())) {
                        String info = "null";
                        try {
                            info = infoTabela.getOrDefault(coluna.getOrDefault("coluna", "null"), "null").toString();
                        }
                        catch (NullPointerException exception){}
                        if (!coluna.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao()) && !info.equals("null")) {
                            info = "'" + info + "'";
                        }
                        sqlInsertLinha.append(info).append(", ");
                    }
                }
                sqlInsertLinha = new StringBuilder(sqlInsertLinha.substring(0, sqlInsertLinha.length() - 2));
                daoUtil.insert(sqlInsertLinha + ")");
            }

            List<Map<String, Object>> idCampoList = daoUtil.select("SELECT MAX(id) AS id FROM CAMPOSCADASTROS", Collections.singletonList("id"));
            configsUtil.salvarConfigs(Integer.parseInt(idCampoList.get(0).get("id").toString()), true);

            janela.dispose();
            Main.getJanelas().add(new Campos(idCadastro));
            JOptionPane.showMessageDialog(null, "Campo adicionado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (CampoException | DaoException exception){
            JOptionPane.showMessageDialog(janela, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
