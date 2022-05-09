package listener.cadastro;

import controle.DaoUtil;
import controle.RegrasCondicionaisUtil;
import controle.enums.*;
import controle.mascaras.DataHoraUtil;
import controle.mascaras.DataUtil;
import exception.DaoException;
import main.Main;
import view.cadastro.Cadastro;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SalvarAlterarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final RegrasCondicionaisUtil regrasCondicionaisUtil;

    private final JFrame janela;
    private final Integer idCadastro;
    private final Integer idRegistro;
    private final Map<String, Object> campos;

    public SalvarAlterarListener(JFrame janela, Integer idCadastro, Integer idRegistro, Map<String, Object> campos, RegrasCondicionaisUtil regrasCondicionaisUtil) {
        this.janela = janela;
        this.idCadastro = idCadastro;
        this.idRegistro = idRegistro;
        this.campos = campos;
        this.regrasCondicionaisUtil = regrasCondicionaisUtil;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String sql;
        String sqlAux = "VALUES (";
        boolean realizarAcao = true;
        String mensagemRetorno = "";

        try {
            List<Map<String, Object>> tabelaCadastro = daoUtil.select(String.format("SELECT tabela FROM CADASTROS WHERE id = %d", idCadastro), Collections.singletonList("tabela"));
            String nomeCadastro = tabelaCadastro.get(0).get("tabela").toString();

            if (idRegistro != null) {
                sql = String.format("UPDATE %s SET ", nomeCadastro);
            } else {
                sql = String.format("INSERT INTO %s (", nomeCadastro);
            }

            for (Map.Entry<String, Object> campo : campos.entrySet()) {
                String coluna = campo.getKey();
                List<Map<String, Object>> infosCampo = daoUtil.select(String.format("SELECT id, label, tipo, obrigatorio FROM CAMPOSCADASTROS WHERE idcadastro = %d AND coluna = '%s'", idCadastro, coluna), Arrays.asList("id", "label", "tipo", "obrigatorio"));

                boolean obrigatorioCondicionalmente = regrasCondicionaisUtil.verificarRegraCondicional(nomeCadastro, Integer.parseInt(infosCampo.get(0).get("id").toString()), idRegistro, TiposRegrasRegrasCondicionaisEnum.OBRIGATORIEDADE);

                if (infosCampo.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.TEXTO.getDescricao())) {
                    if ((infosCampo.get(0).get("obrigatorio") != null && infosCampo.get(0).get("obrigatorio").toString().equalsIgnoreCase("true") && ((JTextField) campo.getValue()).getText().equalsIgnoreCase(""))
                    || obrigatorioCondicionalmente && ((JTextField) campo.getValue()).getText().equalsIgnoreCase("")) {
                        mensagemRetorno = mensagemRetorno + String.format("%s é obrigatório!\n", infosCampo.get(0).get("label").toString());
                        realizarAcao = false;
                    }

                    if (idRegistro != null) {
                        sql = sql + campo.getKey() + String.format(" = '%s' ", ((JTextField) campo.getValue()).getText()) + ", ";
                    } else {
                        sql = sql + campo.getKey() + ", ";
                        sqlAux = sqlAux + "'" + ((JTextField) campo.getValue()).getText() + "', ";
                    }
                } else if (infosCampo.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())) {
                    if (infosCampo.get(0).get("obrigatorio") != null && infosCampo.get(0).get("obrigatorio").toString().equalsIgnoreCase("true") && ((JTextField) campo.getValue()).getText().equalsIgnoreCase("")) {
                        mensagemRetorno = mensagemRetorno + String.format("%s é obrigatório!\n", infosCampo.get(0).get("label").toString());
                        realizarAcao = false;
                    } else if (((JTextField) campo.getValue()).getText().length() > 0) {
                        List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT limite FROM CONFIGSCAMPOSNUMERICO WHERE cadastro = true AND idcampo = %d", Integer.parseInt(infosCampo.get(0).get("id").toString())), Collections.singletonList("limite"));
                        if (configsCampo.get(0).get("limite") != null) {
                            int valor = Integer.parseInt(((JTextField) campo.getValue()).getText());

                            if (configsCampo.get(0).get("limite").toString().equalsIgnoreCase(LimiteNumericoEnum.POSITIVOS.toString())) {
                                if (valor < 0) {
                                    mensagemRetorno = mensagemRetorno + String.format("%s aceita apenas positivos!\n", infosCampo.get(0).get("label").toString());
                                    realizarAcao = false;
                                }
                            } else if (configsCampo.get(0).get("limite").toString().equalsIgnoreCase(LimiteNumericoEnum.NEGATIVOS.getDescricao())) {
                                if (valor > 0) {
                                    mensagemRetorno = mensagemRetorno + String.format("%s aceita apenas negativos!\n", infosCampo.get(0).get("label").toString());
                                    realizarAcao = false;
                                }
                            }
                        }
                    }

                    if (((JTextField) campo.getValue()).getText().length() > 0) {
                        if (idRegistro != null) {
                            sql = sql + campo.getKey() + String.format(" = %s ", ((JTextField) campo.getValue()).getText()) + ", ";
                        } else {
                            sql = sql + campo.getKey() + ", ";
                            sqlAux = sqlAux + ((JTextField) campo.getValue()).getText() + ", ";
                        }
                    }
                } else if (infosCampo.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())) {
                    if (((JTextField) campo.getValue()).getText().length() < 16 && ((JTextField) campo.getValue()).getText().length() > 0) {
                        mensagemRetorno = mensagemRetorno + String.format("Valor inválido para %s", infosCampo.get(0).get("label").toString());
                        realizarAcao = false;
                    } else {
                        if (infosCampo.get(0).get("obrigatorio") != null && infosCampo.get(0).get("obrigatorio").toString().equalsIgnoreCase("true") && ((JTextField) campo.getValue()).getText().equalsIgnoreCase("")) {
                            mensagemRetorno = mensagemRetorno + String.format("%s é obrigatório!\n", infosCampo.get(0).get("label").toString());
                            realizarAcao = false;
                        } else if (((JTextField) campo.getValue()).getText().length() > 0) {
                            List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT limite FROM CONFIGSCAMPOSDATAHORA WHERE cadastro = true AND idcampo = %d", Integer.parseInt(infosCampo.get(0).get("id").toString())), Collections.singletonList("limite"));
                            if (configsCampo.get(0).get("limite") != null) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                                Calendar dataHora = Calendar.getInstance();
                                dataHora.setTime(simpleDateFormat.parse(((JTextField) campo.getValue()).getText()));
                                Calendar hoje = Calendar.getInstance();

                                if (configsCampo.get(0).get("limite").toString().equalsIgnoreCase(LimiteDataHoraEnum.RETROATIVAS.getDescricao())) {
                                    if (dataHora.after(hoje)) {
                                        mensagemRetorno = mensagemRetorno + String.format("%s aceita apenas datas retroativas", infosCampo.get(0).get("label").toString());
                                        realizarAcao = false;
                                    }
                                } else if (configsCampo.get(0).get("limite").toString().equalsIgnoreCase(LimiteDataHoraEnum.POSTERIORES.getDescricao())) {
                                    if (dataHora.before(hoje)) {
                                        mensagemRetorno = mensagemRetorno + String.format("%s aceita apenas datas posteriores", infosCampo.get(0).get("label").toString());
                                        realizarAcao = false;
                                    }
                                }
                            }
                        }
                    }

                    DataHoraUtil dataHoraUtil = new DataHoraUtil(((JTextField) campo.getValue()));
                    if (idRegistro != null) {
                        String sqlInfo;
                        if(((JTextField) campo.getValue()).getText().length() == 0){
                            sqlInfo = " = null ";
                        }
                        else {
                            sqlInfo = String.format(" = '%s' ", dataHoraUtil.converterDataHoraSql());
                        }

                        sql = sql + campo.getKey() + sqlInfo + ", ";
                    } else {
                        sql = sql + campo.getKey() + ", ";

                        String sqlInfo;
                        if(((JTextField) campo.getValue()).getText().length() == 0){
                            sqlInfo = " null ";
                        }
                        else {
                            sqlInfo = String.format(" '%s' ", dataHoraUtil.converterDataHoraSql());
                        }
                        sqlAux = sqlAux + sqlInfo + ", ";
                    }
                } else if (infosCampo.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())) {
                    if (((JTextField) campo.getValue()).getText().length() < 10 && ((JTextField) campo.getValue()).getText().length() > 0) {
                        mensagemRetorno = mensagemRetorno + String.format("Valor inválido para %s", infosCampo.get(0).get("label").toString());
                        realizarAcao = false;
                    } else {
                        if (infosCampo.get(0).get("obrigatorio") != null && infosCampo.get(0).get("obrigatorio").toString().equalsIgnoreCase("true") && ((JTextField) campo.getValue()).getText().equalsIgnoreCase("")) {
                            mensagemRetorno = mensagemRetorno + String.format("%s é obrigatório!\n", infosCampo.get(0).get("label").toString());
                            realizarAcao = false;
                        } else if (((JTextField) campo.getValue()).getText().length() > 0) {
                            List<Map<String, Object>> configsCampo = daoUtil.select(String.format("SELECT limite FROM CONFIGSCAMPOSDATA WHERE cadastro = true AND idcampo = %d", Integer.parseInt(infosCampo.get(0).get("id").toString())), Collections.singletonList("limite"));
                            if (configsCampo.get(0).get("limite") != null) {
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Calendar dataHora = Calendar.getInstance();
                                dataHora.setTime(simpleDateFormat.parse(((JTextField) campo.getValue()).getText()));
                                Calendar hoje = Calendar.getInstance();

                                if (configsCampo.get(0).get("limite").toString().equalsIgnoreCase(LimiteDataEnum.RETROATIVAS.getDescricao())) {
                                    if (dataHora.after(hoje)) {
                                        mensagemRetorno = mensagemRetorno + String.format("%s aceita apenas datas retroativas", infosCampo.get(0).get("label").toString());
                                        realizarAcao = false;
                                    }
                                } else if (configsCampo.get(0).get("limite").toString().equalsIgnoreCase(LimiteDataEnum.POSTERIORES.getDescricao())) {
                                    if (dataHora.before(hoje)) {
                                        mensagemRetorno = mensagemRetorno + String.format("%s aceita apenas datas posteriores", infosCampo.get(0).get("label").toString());
                                        realizarAcao = false;
                                    }
                                }
                            }
                        }
                    }

                    DataUtil dataUtil = new DataUtil((JTextField) campo.getValue());
                    if (idRegistro != null) {
                        String sqlInfo;
                        if(((JTextField) campo.getValue()).getText().length() == 0){
                            sqlInfo = " = null ";
                        }
                        else {
                            sqlInfo = String.format(" = '%s' ", dataUtil.converterDataSql());
                        }

                        sql = sql + campo.getKey() + sqlInfo + ", ";
                    } else {
                        sql = sql + campo.getKey() + ", ";

                        String sqlInfo;
                        if(((JTextField) campo.getValue()).getText().length() == 0){
                            sqlInfo = " null ";
                        }
                        else {
                            sqlInfo = String.format(" '%s' ", dataUtil.converterDataSql());
                        }
                        sqlAux = sqlAux + sqlInfo + ", ";
                    }
                } else if (infosCampo.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())) {
                    if (infosCampo.get(0).get("obrigatorio") != null && infosCampo.get(0).get("obrigatorio").toString().equalsIgnoreCase("true") && !((JCheckBox) campo.getValue()).isSelected()) {
                        mensagemRetorno = mensagemRetorno + String.format("%s é obrigatório!\n", infosCampo.get(0).get("label").toString());
                        realizarAcao = false;
                    }

                    if (idRegistro != null) {
                        sql = sql + campo.getKey() + String.format(" = %s ", ((JCheckBox) campo.getValue()).isSelected()) + ", ";
                    } else {
                        sql = sql + campo.getKey() + ", ";
                        sqlAux = sqlAux + ((JCheckBox) campo.getValue()).isSelected() + ", ";
                    }
                } else if (infosCampo.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.AREATEXTO.getDescricao())) {
                    if (infosCampo.get(0).get("obrigatorio") != null && infosCampo.get(0).get("obrigatorio").toString().equalsIgnoreCase("true") && ((JTextArea) campo.getValue()).getText().equalsIgnoreCase("")) {
                        mensagemRetorno = mensagemRetorno + String.format("%s é obrigatório!\n", infosCampo.get(0).get("label").toString());
                        realizarAcao = false;
                    }

                    if (idRegistro != null) {
                        sql = sql + campo.getKey() + String.format(" = '%s' ", ((JTextArea) campo.getValue()).getText()) + ", ";
                    } else {
                        sql = sql + campo.getKey() + ", ";
                        sqlAux = sqlAux + "'" + ((JTextArea) campo.getValue()).getText() + "', ";
                    }
                } else if (infosCampo.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.COMBOBOX.getDescricao())) {
                    if (infosCampo.get(0).get("obrigatorio") != null && infosCampo.get(0).get("obrigatorio").toString().equalsIgnoreCase("true") && ((JComboBox<String>) campo.getValue()).getSelectedItem().toString().equalsIgnoreCase(OpcaoComboEnum.OPCAO_COMBOBOX_PADRAO.getDescricao())) {
                        mensagemRetorno = mensagemRetorno + String.format("%s é obrigatório!\n", infosCampo.get(0).get("label").toString());
                        realizarAcao = false;
                    }

                    if (!((JComboBox<String>) campo.getValue()).getSelectedItem().toString().equalsIgnoreCase(OpcaoComboEnum.OPCAO_COMBOBOX_PADRAO.getDescricao())) {
                        if (idRegistro != null) {
                            sql = sql + campo.getKey() + String.format(" = '%s' ", ((JComboBox<String>) campo.getValue()).getSelectedItem()) + ", ";
                        } else {
                            sql = sql + campo.getKey() + ", ";
                            sqlAux = sqlAux + "'" + ((JComboBox<String>) campo.getValue()).getSelectedItem() + "', ";
                        }
                    }
                } else if (infosCampo.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())) {
                    if (((ButtonGroup) campo.getValue()).getSelection() != null) {
                        if (idRegistro != null) {
                            sql = sql + campo.getKey() + String.format(" = '%s' ", ((ButtonGroup) campo.getValue()).getSelection().getActionCommand()) + ", ";
                        } else {
                            sql = sql + campo.getKey() + ", ";
                            sqlAux = sqlAux + "'" + ((ButtonGroup) campo.getValue()).getSelection().getActionCommand() + "', ";
                        }
                    } else if (infosCampo.get(0).get("obrigatorio") != null && infosCampo.get(0).get("obrigatorio").toString().equalsIgnoreCase("true")) {
                        mensagemRetorno = mensagemRetorno + String.format("%s é obrigatório!\n", infosCampo.get(0).get("label").toString());
                        realizarAcao = false;
                    }
                }
            }

            if (realizarAcao) {
                String mensagem;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                if (idRegistro != null) {
                    mensagem = "Registro alterado";
                    sql = sql + String.format("ultima_atualizacao = '%s', atualizado_por = %d, ", sdf.format(timestamp), Main.getUsuarioLogado().getId());
                    sql = sql.substring(0, sql.length() - 2) + String.format(" WHERE id = %d", idRegistro);
                } else {
                    mensagem = "Registro adicionado";
                    sql = sql + "registrado_em, registrado_por, ";
                    sql = sql.substring(0, sql.length() - 2) + ") ";
                    sqlAux = sqlAux + String.format("'%s', %d, ", sdf.format(timestamp), Main.getUsuarioLogado().getId());
                    sqlAux = sqlAux.substring(0, sqlAux.length() - 2) + ")";
                    sql = sql + sqlAux;
                }

                daoUtil.update(sql);
                janela.dispose();
                Main.getJanelas().add(new Cadastro(idCadastro));
                JOptionPane.showMessageDialog(null, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, mensagemRetorno, "Atenção", JOptionPane.WARNING_MESSAGE);
            }
        } catch (DaoException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException exception) {
            JOptionPane.showMessageDialog(null, "Falha ao converter data", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
