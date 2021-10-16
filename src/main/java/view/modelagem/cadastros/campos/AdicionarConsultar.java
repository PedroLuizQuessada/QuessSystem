package view.modelagem.cadastros.campos;

import controle.ComboboxUtil;
import controle.ConfigsUtil;
import controle.DaoUtil;
import controle.JFrameUtil;
import controle.enums.LimiteDataEnum;
import controle.enums.LimiteDataHoraEnum;
import controle.enums.LimiteNumericoEnum;
import controle.enums.TipoCampoEnum;
import controle.mascaras.DataHoraUtil;
import controle.mascaras.DataUtil;
import controle.mascaras.NumericoUtil;
import exception.DaoException;
import listener.home.VoltarListener;
import listener.modelagem.cadastros.campos.AlterarListener;
import listener.modelagem.cadastros.campos.CriarListener;
import listener.modelagem.cadastros.campos.TipoListener;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AdicionarConsultar extends JFrame {
    private final DaoUtil daoUtil = new DaoUtil();
    private final ComboboxUtil comboboxUtil = new ComboboxUtil();
    private final JFrameUtil jFrameUtil = new JFrameUtil();

    private final JComboBox<String> ordem = new JComboBox<>();
    private final JTextField label = new JTextField();
    private final JTextField coluna = new JTextField();
    private final JComboBox<String> tipo = new JComboBox<>();
    private final JButton jButton = new JButton("Alterar");
    private final GridBagConstraints c = new GridBagConstraints();

    private final ConfigsUtil configsUtil = new ConfigsUtil(tipo);
    private final List<JComponent> camposConfigs = new ArrayList<>();

    private CriarListener criarListener;
    private AlterarListener alterarListener;

    public AdicionarConsultar(Integer idCadastro, Integer idCampo){
        String titulo = "Consultar Campo";
        setLayout(new GridBagLayout());

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Ordem"), c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridx++;
        add(new JLabel("Label"), c);

        c.gridx++;
        add(new JLabel("Coluna"), c);

        c.gridx++;
        add(new JLabel("Tipo"), c);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        add(ordem, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridx++;
        label.setColumns(7);
        add(label, c);

        c.gridx++;
        coluna.setColumns(7);
        add(coluna, c);

        c.gridx++;
        comboboxUtil.carregarTiposCampos(tipo);
        tipo.addActionListener(new TipoListener(tipo, camposConfigs, this, titulo, idCadastro, idCampo));
        add(tipo, c);

        try {
            carregarConfigs(camposConfigs, tipo.getSelectedItem().toString(), titulo, idCadastro, idCampo);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void carregarConfigs(List<JComponent> camposConfigs, String tipoCampo, String titulo, Integer idCadastro, Integer idCampo) throws DaoException {
        Insets insets = new Insets(-200, 0, 0, -800);
        for(JComponent campoConfig: camposConfigs){
            remove(campoConfig);
        }
        camposConfigs.clear();

        if(tipoCampo.equalsIgnoreCase(TipoCampoEnum.TEXTO.getDescricao())) {
            insets = new Insets(-345, 0, 0, -800);
            carrregarConfigsTexto(camposConfigs);
        }
        else if (tipoCampo.equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())) {
            insets = new Insets(-350, 0, 0, -800);
            carrregarConfigsNumerico(camposConfigs);
        }
        else if (tipoCampo.equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())) {
            insets = new Insets(-350, 0, 0, -800);
            carrregarConfigsDataHora(camposConfigs);
        }
        else if (tipoCampo.equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())) {
            insets = new Insets(-350, 0, 0, -800);
            carrregarConfigsData(camposConfigs);
        }

        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 2;
        add(jButton, c);

        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(new VoltarListener(this, idCadastro));
        c.insets = insets;
        add(voltar, c);
        camposConfigs.add(voltar);

        if (idCampo == null) {
            comboboxUtil.carregarOrdem(idCadastro, ordem, true);
            titulo = "Criar Campo";
            jButton.setText("Criar campo");
            jButton.removeActionListener(criarListener);
            criarListener = new CriarListener(this, idCadastro, idCampo, configsUtil);
            jButton.addActionListener(criarListener);
        } else {
            ordem.removeAllItems();
            comboboxUtil.carregarOrdem(idCadastro, ordem, false);
            carregarInfosCampo(idCampo);
            coluna.setEnabled(false);
            tipo.setEnabled(false);
            jButton.removeActionListener(alterarListener);
            alterarListener = new AlterarListener(this, idCadastro, idCampo, configsUtil);
            jButton.addActionListener(alterarListener);
        }

        jFrameUtil.configurarJanela(this, Main.getImageIcon(), titulo, 1000, 300);
    }

    private void carrregarConfigsTexto(List<JComponent> camposConfigs){
        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 2;
        JLabel valorPadraoLabel = new JLabel("Valor padrão");
        camposConfigs.add(valorPadraoLabel);
        add(valorPadraoLabel, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridy++;
        configsUtil.setValorPadrao(new JTextField());
        camposConfigs.add(configsUtil.getValorPadrao());
        configsUtil.getValorPadrao().setColumns(7);
        add(configsUtil.getValorPadrao(), c);
    }

    private void carrregarConfigsNumerico(List<JComponent> camposConfigs){
        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        JLabel limiteLabel = new JLabel("Limite do número");
        camposConfigs.add(limiteLabel);
        add(limiteLabel, c);

        c.gridx = 3;
        JLabel valorPadraoLabel = new JLabel("Valor padrão");
        camposConfigs.add(valorPadraoLabel);
        add(valorPadraoLabel, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        configsUtil.setLimite(new JComboBox<>());
        camposConfigs.add(configsUtil.getLimite());
        for(LimiteNumericoEnum limiteNumericoEnum: LimiteNumericoEnum.values()){
            configsUtil.getLimite().addItem(limiteNumericoEnum.getDescricao());
        }
        configsUtil.getLimite().setSelectedItem(LimiteNumericoEnum.PADRAO.getDescricao());
        add(configsUtil.getLimite(), c);

        c.gridx = 3;
        configsUtil.setValorPadrao(new JTextField());
        camposConfigs.add(configsUtil.getValorPadrao());
        configsUtil.getValorPadrao().addKeyListener(new NumericoUtil(configsUtil.getValorPadrao()));
        configsUtil.getValorPadrao().setColumns(7);
        add(configsUtil.getValorPadrao(), c);
    }

    private void carrregarConfigsDataHora(List<JComponent> camposConfigs){
        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        JLabel limiteLabel = new JLabel("Limite da data");
        camposConfigs.add(limiteLabel);
        add(limiteLabel, c);

        c.gridx = 3;
        JLabel valorPadraoLabel = new JLabel("Valor padrão");
        camposConfigs.add(valorPadraoLabel);
        add(valorPadraoLabel, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        configsUtil.setLimite(new JComboBox<>());
        camposConfigs.add(configsUtil.getLimite());
        for(LimiteDataHoraEnum limiteDataHoraEnum: LimiteDataHoraEnum.values()){
            configsUtil.getLimite().addItem(limiteDataHoraEnum.getDescricao());
        }
        configsUtil.getLimite().setSelectedItem(LimiteDataHoraEnum.PADRAO.getDescricao());
        add(configsUtil.getLimite(), c);

        c.gridx = 3;
        configsUtil.setValorPadrao(new JTextField());
        camposConfigs.add(configsUtil.getValorPadrao());
        configsUtil.getValorPadrao().addKeyListener(new DataHoraUtil(configsUtil.getValorPadrao()));
        configsUtil.getValorPadrao().setColumns(7);
        add(configsUtil.getValorPadrao(), c);
    }

    private void carrregarConfigsData(List<JComponent> camposConfigs){
        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        JLabel limiteLabel = new JLabel("Limite da data");
        camposConfigs.add(limiteLabel);
        add(limiteLabel, c);

        c.gridx = 3;
        JLabel valorPadraoLabel = new JLabel("Valor padrão");
        camposConfigs.add(valorPadraoLabel);
        add(valorPadraoLabel, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        configsUtil.setLimite(new JComboBox<>());
        camposConfigs.add(configsUtil.getLimite());
        for(LimiteDataEnum limiteDataEnum: LimiteDataEnum.values()){
            configsUtil.getLimite().addItem(limiteDataEnum.getDescricao());
        }
        configsUtil.getLimite().setSelectedItem(LimiteDataHoraEnum.PADRAO.getDescricao());
        add(configsUtil.getLimite(), c);

        c.gridx = 3;
        configsUtil.setValorPadrao(new JTextField());
        camposConfigs.add(configsUtil.getValorPadrao());
        configsUtil.getValorPadrao().addKeyListener(new DataUtil(configsUtil.getValorPadrao()));
        configsUtil.getValorPadrao().setColumns(7);
        add(configsUtil.getValorPadrao(), c);
    }

    private void carregarInfosCampo(Integer idCampo) throws DaoException{
        List<Map<String, Object>> infosConfigsList;
        List<Map<String, Object>> infosCampoList = daoUtil.select(String.format("SELECT ordem, label, coluna, tipo, nativo FROM CAMPOSCADASTROS WHERE id = %d", idCampo), Arrays.asList("ordem", "label", "coluna", "tipo", "nativo"));

        ordem.setSelectedItem(infosCampoList.get(0).get("ordem").toString());
        label.setText(infosCampoList.get(0).get("label").toString());
        coluna.setText(infosCampoList.get(0).get("coluna").toString());
        tipo.setSelectedItem(infosCampoList.get(0).get("tipo").toString());

        if(infosCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.TEXTO.getDescricao())) {
            infosConfigsList = daoUtil.select(String.format("SELECT * FROM CONFIGSCAMPOSTEXTO WHERE idcampo = %d AND cadastro = true", idCampo), Collections.singletonList("valorpadrao"));

            if (infosConfigsList.get(0).get("valorpadrao") != null) {
                configsUtil.getValorPadrao().setText(infosConfigsList.get(0).get("valorpadrao").toString());
            }

            if (infosCampoList.get(0).get("nativo") != null) {
                configsUtil.getValorPadrao().setEnabled(false);
            }
        }
        else if (infosCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())) {
            infosConfigsList = daoUtil.select(String.format("SELECT * FROM CONFIGSCAMPOSNUMERICO WHERE idcampo = %d AND cadastro = true", idCampo), Arrays.asList("valorpadrao", "limite"));

            if (infosConfigsList.get(0).get("valorpadrao") != null) {
                configsUtil.getValorPadrao().setText(infosConfigsList.get(0).get("valorpadrao").toString());
            }
            if (infosConfigsList.get(0).get("limite") != null) {
                configsUtil.getLimite().setSelectedItem(infosConfigsList.get(0).get("limite").toString());
            }

            if (infosCampoList.get(0).get("nativo") != null) {
                configsUtil.getValorPadrao().setEnabled(false);
                configsUtil.getLimite().setEnabled(false);
            }
        }
        else if (infosCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())) {
            infosConfigsList = daoUtil.select(String.format("SELECT * FROM CONFIGSCAMPOSDATAHORA WHERE idcampo = %d AND cadastro = true", idCampo), Arrays.asList("valorpadrao", "limite"));

            if (infosConfigsList.get(0).get("valorpadrao") != null) {
                configsUtil.getValorPadrao().setText(infosConfigsList.get(0).get("valorpadrao").toString());
            }
            if (infosConfigsList.get(0).get("limite") != null) {
                configsUtil.getLimite().setSelectedItem(infosConfigsList.get(0).get("limite").toString());
            }

            if (infosCampoList.get(0).get("nativo") != null) {
                configsUtil.getValorPadrao().setEnabled(false);
                configsUtil.getLimite().setEnabled(false);
            }
        }
        else if (infosCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())) {
            infosConfigsList = daoUtil.select(String.format("SELECT * FROM CONFIGSCAMPOSDATA WHERE idcampo = %d AND cadastro = true", idCampo), Arrays.asList("valorpadrao", "limite"));

            if (infosConfigsList.get(0).get("valorpadrao") != null) {
                configsUtil.getValorPadrao().setText(infosConfigsList.get(0).get("valorpadrao").toString());
            }
            if (infosConfigsList.get(0).get("limite") != null) {
                configsUtil.getLimite().setSelectedItem(infosConfigsList.get(0).get("limite").toString());
            }

            if (infosCampoList.get(0).get("nativo") != null) {
                configsUtil.getValorPadrao().setEnabled(false);
                configsUtil.getLimite().setEnabled(false);
            }
        }
    }

    public JComboBox<String> getOrdem() {
        return ordem;
    }

    public JTextField getLabel() {
        return label;
    }

    public JTextField getColuna() {
        return coluna;
    }

    public JComboBox<String> getTipo() {
        return tipo;
    }
}
