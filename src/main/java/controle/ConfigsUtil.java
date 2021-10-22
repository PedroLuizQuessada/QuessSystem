package controle;

import controle.enums.TipoCampoEnum;
import exception.DaoException;

import javax.swing.*;

public class ConfigsUtil {
    private final DaoUtil daoUtil = new DaoUtil();

    private final JComboBox<String> tipoCampo;
    private JTextField valorPadrao = null;
    private JComboBox<String> limite = null;
    private JCheckBox estadoPadrao = null;
    private JTextArea valorPadraoArea = null;
    private JTextField limiteCaracteres = null;
    private JTextField novaOpcao = null;
    private JComboBox<String> opcoesAdicionadas = null;
    private JComboBox<String> opcaoPadrao = null;
    private JButton adicionarOpcao = null;
    private JButton removerOpcao = null;
    private JCheckBox ordenacaoDesc = null;
    private JComboBox<String> campoOrdenador = null;
    private JComboBox<String> agrupador = null;

    public ConfigsUtil(JComboBox<String> tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    public void salvarConfigs(int idCampo, boolean cadastro) throws DaoException {
        if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.TEXTO.getDescricao())) {
            daoUtil.insert(String.format("INSERT INTO CONFIGSCAMPOSTEXTO (valorpadrao, idcampo, cadastro) VALUES ('%s', %d, %s)", valorPadrao.getText(), idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())) {
            String numPadrao = valorPadrao.getText();
            if (numPadrao.length() == 0) {
                numPadrao = "null";
            }
            daoUtil.insert(String.format("INSERT INTO CONFIGSCAMPOSNUMERICO (valorpadrao, limite, idcampo, cadastro) VALUES (%s, '%s', %d, %s)", numPadrao, limite.getSelectedItem().toString(), idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())) {
            daoUtil.insert(String.format("INSERT INTO CONFIGSCAMPOSDATAHORA (valorpadrao, limite, idcampo, cadastro) VALUES ('%s', '%s', %d, %s)", valorPadrao.getText(), limite.getSelectedItem().toString(), idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())) {
            daoUtil.insert(String.format("INSERT INTO CONFIGSCAMPOSDATA (valorpadrao, limite, idcampo, cadastro) VALUES ('%s', '%s', %d, %s)", valorPadrao.getText(), limite.getSelectedItem().toString(), idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())) {
            daoUtil.insert(String.format("INSERT INTO CONFIGSCAMPOSCHECKBOX (estadopadrao, idcampo, cadastro) VALUES (%s, %d, %s)", estadoPadrao.isSelected(), idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.AREATEXTO.getDescricao())) {
            String limiteCaracteresTexto = limiteCaracteres.getText();
            if (limiteCaracteresTexto.length() == 0) {
                limiteCaracteresTexto = "null";
            }
            daoUtil.insert(String.format("INSERT INTO CONFIGSCAMPOSAREATEXTO (valorpadrao, limitecaracteres, idcampo, cadastro) VALUES ('%s', %s, %d, %s)", valorPadraoArea.getText(), limiteCaracteresTexto, idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.COMBOBOX.getDescricao())) {
            daoUtil.insert(String.format("INSERT INTO CONFIGSCAMPOSCOMBOBOX (idcampo, cadastro) VALUES (%d, %s)", idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())) {
            daoUtil.insert(String.format("INSERT INTO CONFIGSCAMPOSRADIO (idcampo, cadastro) VALUES (%d, %s)", idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.AGRUPADOR.getDescricao())) {
            String tipoAgr = "";
            if(cadastro){
                tipoAgr = "cad";
            }

            daoUtil.create(String.format("CREATE TABLE agrupador_%s_%d " +
                    "(id INTEGER NOT NULL IDENTITY, " +
                    "\nid_rel INTEGER NOT NULL, " +
                    "\nid_usuario INTEGER NOT NULL, " +
                    "\nPRIMARY KEY(id))", tipoAgr, idCampo));

            daoUtil.insert(String.format("INSERT INTO CONFIGSCAMPOSAGRUPADOR (idcampo, cadastro, ordenacaodesc) VALUES (%d, %s, false)", idCampo, cadastro));
        }
    }

    public void alterarConfigs(int idCampo, boolean cadastro) throws DaoException {
        if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.TEXTO.getDescricao())) {
            daoUtil.update(String.format("UPDATE CONFIGSCAMPOSTEXTO SET valorpadrao = '%s' WHERE idcampo = %d AND cadastro = %s", valorPadrao.getText(), idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())) {
            String numPadrao = valorPadrao.getText();
            if (numPadrao.length() == 0) {
                numPadrao = "null";
            }
            daoUtil.update(String.format("UPDATE CONFIGSCAMPOSNUMERICO SET valorpadrao = %s, limite = '%s' WHERE idcampo = %d AND cadastro = %s", numPadrao, limite.getSelectedItem().toString(), idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())) {
            daoUtil.update(String.format("UPDATE CONFIGSCAMPOSDATAHORA SET valorpadrao = '%s', limite = '%s' WHERE idcampo = %d AND cadastro = %s", valorPadrao.getText(), limite.getSelectedItem().toString(), idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())) {
            daoUtil.update(String.format("UPDATE CONFIGSCAMPOSDATA SET valorpadrao = '%s', limite = '%s' WHERE idcampo = %d AND cadastro = %s", valorPadrao.getText(), limite.getSelectedItem().toString(), idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())) {
            daoUtil.update(String.format("UPDATE CONFIGSCAMPOSCHECKBOX SET estadopadrao = %s WHERE idcampo = %d AND cadastro = %s", estadoPadrao.isSelected(), idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.AREATEXTO.getDescricao())) {
            String limiteCaracteresTexto = limiteCaracteres.getText();
            if (limiteCaracteresTexto.length() == 0) {
                limiteCaracteresTexto = "null";
            }
            daoUtil.update(String.format("UPDATE CONFIGSCAMPOSAREATEXTO SET valorpadrao = '%s', limitecaracteres = %s WHERE idcampo = %d AND cadastro = %s", valorPadraoArea.getText(), limiteCaracteresTexto, idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.COMBOBOX.getDescricao())) {
            daoUtil.update(String.format("UPDATE CONFIGSCAMPOSCOMBOBOX SET opcaopadrao = '%s' WHERE idcampo = %d AND cadastro = %s", opcaoPadrao.getSelectedItem(), idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())) {
            daoUtil.update(String.format("UPDATE CONFIGSCAMPOSRADIO SET opcaopadrao = '%s' WHERE idcampo = %d AND cadastro = %s", opcaoPadrao.getSelectedItem(), idCampo, cadastro));
        }
        else if(tipoCampo.getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.AGRUPADOR.getDescricao())) {
            daoUtil.update(String.format("UPDATE CONFIGSCAMPOSAGRUPADOR SET ordenacaodesc = %s, ordenacaocampo = '%s' WHERE idcampo = %d AND cadastro = %s", ordenacaoDesc.isSelected(), campoOrdenador.getSelectedItem(), idCampo, cadastro));
        }
    }

    public JTextField getValorPadrao() {
        return valorPadrao;
    }

    public void setValorPadrao(JTextField valorPadrao) {
        this.valorPadrao = valorPadrao;
    }

    public JComboBox<String> getLimite() {
        return limite;
    }

    public void setLimite(JComboBox<String> limite) {
        this.limite = limite;
    }

    public JCheckBox getEstadoPadrao() {
        return estadoPadrao;
    }

    public void setEstadoPadrao(JCheckBox estadoPadrao) {
        this.estadoPadrao = estadoPadrao;
    }

    public JTextArea getValorPadraoArea() {
        return valorPadraoArea;
    }

    public void setValorPadraoArea(JTextArea valorPadraoArea) {
        this.valorPadraoArea = valorPadraoArea;
    }

    public JTextField getLimiteCaracteres() {
        return limiteCaracteres;
    }

    public void setLimiteCaracteres(JTextField limiteCaracteres) {
        this.limiteCaracteres = limiteCaracteres;
    }

    public JTextField getNovaOpcao() {
        return novaOpcao;
    }

    public void setNovaOpcao(JTextField novaOpcao) {
        this.novaOpcao = novaOpcao;
    }

    public JComboBox<String> getOpcoesAdicionadas() {
        return opcoesAdicionadas;
    }

    public void setOpcoesAdicionadas(JComboBox<String> opcoesAdicionadas) {
        this.opcoesAdicionadas = opcoesAdicionadas;
    }

    public JComboBox<String> getOpcaoPadrao() {
        return opcaoPadrao;
    }

    public void setOpcaoPadrao(JComboBox<String> opcaoPadrao) {
        this.opcaoPadrao = opcaoPadrao;
    }

    public JButton getAdicionarOpcao() {
        return adicionarOpcao;
    }

    public void setAdicionarOpcao(JButton adicionarOpcao) {
        this.adicionarOpcao = adicionarOpcao;
    }

    public JButton getRemoverOpcao() {
        return removerOpcao;
    }

    public void setRemoverOpcao(JButton removerOpcao) {
        this.removerOpcao = removerOpcao;
    }

    public JCheckBox getOrdenacaoDesc() {
        return ordenacaoDesc;
    }

    public void setOrdenacaoDesc(JCheckBox ordenacaoDesc) {
        this.ordenacaoDesc = ordenacaoDesc;
    }

    public JComboBox<String> getCampoOrdenador() {
        return campoOrdenador;
    }

    public void setCampoOrdenador(JComboBox<String> campoOrdenador) {
        this.campoOrdenador = campoOrdenador;
    }

    public JComboBox<String> getAgrupador() {
        return agrupador;
    }

    public void setAgrupador(JComboBox<String> agrupador) {
        this.agrupador = agrupador;
    }
}
