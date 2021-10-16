package controle;

import controle.enums.TipoCampoEnum;
import exception.DaoException;

import javax.swing.*;

public class ConfigsUtil {
    private final DaoUtil daoUtil = new DaoUtil();

    private final JComboBox<String> tipoCampo;
    private JTextField valorPadrao = null;
    private JComboBox<String> limite = null;

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
}
