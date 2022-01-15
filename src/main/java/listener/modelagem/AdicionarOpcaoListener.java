package listener.modelagem;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import controle.validacoes.OpcaoUtil;
import exception.DaoException;
import exception.validacoes.OpcaoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdicionarOpcaoListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final OpcaoUtil opcaoUtil = new OpcaoUtil();

    private final int idCampo;
    private final boolean cadastro;
    private final JComboBox<String> opcoesAdicionadas;
    private final JComboBox<String> opcaoPadrao;
    private final JTextField novaOpcao;
    private final boolean combobox;

    public AdicionarOpcaoListener(int idCampo, boolean cadastro, JComboBox<String> opcoesAdicionadas, JComboBox<String> opcaoPadrao, JTextField novaOpcao, boolean combobox) {
        this.idCampo = idCampo;
        this.cadastro = cadastro;
        this.opcoesAdicionadas = opcoesAdicionadas;
        this.opcaoPadrao = opcaoPadrao;
        this.novaOpcao = novaOpcao;
        this.combobox = combobox;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String tabela = "CONFIGSCAMPOSCOMBOBOX";
            if(!combobox){
                tabela = "CONFIGSCAMPOSRADIO";
            }

            String novaOpcaoTexto = novaOpcao.getText();
            opcaoUtil.validarOpcao(idCampo, cadastro, novaOpcao, tabela);

            opcoesAdicionadas.addItem(novaOpcaoTexto);
            opcaoPadrao.addItem(novaOpcaoTexto);
            novaOpcao.setText("");

            String opcoes = "";

            for(int i = 0; i < opcoesAdicionadas.getItemCount(); i++){
                if(!opcoesAdicionadas.getItemAt(i).equals(OpcaoComboEnum.OPCAO_COMBOBOX_PADRAO.getDescricao())) {
                    opcoes = opcoes + opcoesAdicionadas.getItemAt(i) + "_";
                }
            }
            if(opcoes.length() > 0) {
                opcoes = opcoes.substring(0, opcoes.length() - 1);
            }

            daoUtil.update(String.format("UPDATE %s SET opcoes = '%s' WHERE idcampo = %d AND cadastro = %s", tabela, opcoes, idCampo, cadastro));
            JOptionPane.showMessageDialog(null, "Opção adicionada", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException | OpcaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
