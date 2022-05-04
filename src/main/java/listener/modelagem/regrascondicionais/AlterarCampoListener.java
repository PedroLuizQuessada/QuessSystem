package listener.modelagem.regrascondicionais;

import controle.DaoUtil;
import controle.enums.OpcoesPadraoRegrasCondicionaisEnum;
import controle.enums.TipoCampoEnum;
import exception.DaoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class AlterarCampoListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final boolean cadastro;
    private final JComboBox<String> campo;
    private final JComboBox<String> infosAdicionadas;
    private final JComboBox<String> operador;

    public AlterarCampoListener(boolean cadastro, JComboBox<String> campo, JComboBox<String> infosAdicionadas, JComboBox<String> operador) {
        this.cadastro = cadastro;
        this.campo = campo;
        this.infosAdicionadas = infosAdicionadas;
        this.operador = operador;

        try {
            carregarInfos();
        } catch (DaoException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            carregarInfos();
            operador.setSelectedItem(OpcoesPadraoRegrasCondicionaisEnum.OPERADOR.getDescricao());
        } catch (DaoException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarInfos() throws DaoException {
        infosAdicionadas.removeAllItems();
        infosAdicionadas.addItem(OpcoesPadraoRegrasCondicionaisEnum.VALOR.getDescricao());
        infosAdicionadas.setSelectedItem(OpcoesPadraoRegrasCondicionaisEnum.VALOR.getDescricao());

        String campoSelecionado = (String) campo.getSelectedItem();
        if (!campoSelecionado.equals(OpcoesPadraoRegrasCondicionaisEnum.CAMPO.getDescricao())) {
            Integer idCampo;
            String tipoCampo;
            List<Map<String, Object>> idCampoList = new ArrayList<>();
            List<Map<String, Object>> infos = new ArrayList<>();
            if (cadastro) {
                idCampoList = daoUtil.select(String.format("SELECT id, tipo FROM camposcadastros WHERE label = '%s'", campoSelecionado), Arrays.asList("id", "tipo"));
            }

            if (!idCampoList.isEmpty()) {
                tipoCampo = (String) idCampoList.get(0).get("tipo");
                idCampo = Integer.parseInt(idCampoList.get(0).get("id").toString());

                if (tipoCampo.equals(TipoCampoEnum.CHECKBOX.getDescricao())) {
                    infosAdicionadas.addItem("Sim");
                    infosAdicionadas.addItem("Não");
                } else if (tipoCampo.equals(TipoCampoEnum.RADIO.getDescricao())) {
                    infos = daoUtil.select(String.format("SELECT opcoes FROM CONFIGSCAMPOSRADIO WHERE idcampo = %d AND cadastro = %s", idCampo, cadastro), Collections.singletonList("opcoes"));
                } else if (tipoCampo.equals(TipoCampoEnum.COMBOBOX.getDescricao())) {
                    infos = daoUtil.select(String.format("SELECT opcoes FROM CONFIGSCAMPOSCOMBOBOX WHERE idcampo = %d AND cadastro = %s", idCampo, cadastro), Collections.singletonList("opcoes"));
                }

                if (!infos.isEmpty()) {
                    if (infos.get(0).get("opcoes") != null) {
                        String opcoes = (String) infos.get(0).get("opcoes");
                        String[] opcoesArray = opcoes.split("_");

                        for (String opcao : opcoesArray) {
                            infosAdicionadas.addItem(opcao);
                        }
                    }
                }
            }
        }
    }
}
