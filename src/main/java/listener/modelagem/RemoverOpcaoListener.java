package listener.modelagem;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;
import exception.validacoes.OpcaoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoverOpcaoListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final int idCampo;
    private final boolean cadastro;
    private final JComboBox<String> opcoesAdicionadas;
    private final JComboBox<String> opcaoPadrao;
    private final boolean combobox;

    public RemoverOpcaoListener(int idCampo, boolean cadastro, JComboBox<String> opcoesAdicionadas, JComboBox<String> opcaoPadrao, boolean combobox) {
        this.idCampo = idCampo;
        this.cadastro = cadastro;
        this.opcoesAdicionadas = opcoesAdicionadas;
        this.opcaoPadrao = opcaoPadrao;
        this.combobox = combobox;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String tabela = "CONFIGSCAMPOSCOMBOBOX";
            if(!combobox){
                tabela = "CONFIGSCAMPOSRADIO";
            }

            if(opcoesAdicionadas.getSelectedItem().equals(OpcaoComboEnum.OPCAO_COMBO_RADIO.getDescricao())){
                throw new OpcaoException("Necessário escolher uma opção para remover");
            }

            String opcoes = "";

            for(int i = 0; i < opcoesAdicionadas.getItemCount(); i++){
                if(!opcoesAdicionadas.getItemAt(i).equals(OpcaoComboEnum.OPCAO_COMBO_RADIO.getDescricao()) && !opcoesAdicionadas.getItemAt(i).equals(opcoesAdicionadas.getSelectedItem())) {
                    opcoes = opcoes + opcoesAdicionadas.getItemAt(i) + "_";
                }
            }
            if(opcoes.length() > 0) {
                opcoes = opcoes.substring(0, opcoes.length() - 1);
            }

            String sqlOpcaoPadrao = "";
            if(opcaoPadrao.getSelectedItem().equals(opcoesAdicionadas.getSelectedItem())){
                opcaoPadrao.setSelectedItem(OpcaoComboEnum.SEM_OPCAO_PADRAO.getDescricao());
                sqlOpcaoPadrao = ", opcaopadrao = null ";
            }
            daoUtil.update(String.format("UPDATE %s SET opcoes = '%s' %s WHERE idcampo = %d AND cadastro = %s", tabela, opcoes, sqlOpcaoPadrao, idCampo, cadastro));

            opcaoPadrao.removeItem(opcoesAdicionadas.getSelectedItem());
            opcoesAdicionadas.removeItem(opcoesAdicionadas.getSelectedItem());
            opcoesAdicionadas.setSelectedItem(OpcaoComboEnum.OPCAO_COMBO_RADIO.getDescricao());

            JOptionPane.showMessageDialog(null, "Opção removida", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException | OpcaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
