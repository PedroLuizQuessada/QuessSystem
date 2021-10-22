package view.modelagem.cadastros.campos;

import controle.*;
import controle.enums.*;
import controle.mascaras.DataHoraUtil;
import controle.mascaras.DataUtil;
import controle.mascaras.NumericoUtil;
import exception.DaoException;
import listener.home.VoltarListener;
import listener.modelagem.AdicionarOpcaoListener;
import listener.modelagem.RemoverOpcaoListener;
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
    private final TextAreaUtil textAreaUtil = new TextAreaUtil();
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
            carrregarConfigsTexto(camposConfigs, idCampo);
        }
        else if (tipoCampo.equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())) {
            insets = new Insets(-350, 0, 0, -800);
            carrregarConfigsNumerico(camposConfigs, idCampo);
        }
        else if (tipoCampo.equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())) {
            insets = new Insets(-350, 0, 0, -800);
            carrregarConfigsDataHora(camposConfigs, idCampo);
        }
        else if (tipoCampo.equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())) {
            insets = new Insets(-350, 0, 0, -800);
            carrregarConfigsData(camposConfigs, idCampo);
        }
        else if (tipoCampo.equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())) {
            insets = new Insets(-345, 0, 0, -800);
            carrregarConfigsCheckbox(camposConfigs, idCampo);
        }
        else if (tipoCampo.equalsIgnoreCase(TipoCampoEnum.AREATEXTO.getDescricao())) {
            insets = new Insets(-390, 0, 0, -950);
            carrregarConfigsAreaTexto(camposConfigs, idCampo);
        }
        else if (tipoCampo.equalsIgnoreCase(TipoCampoEnum.COMBOBOX.getDescricao())) {
            insets = new Insets(-350, 0, 0, -800);
            c.insets = new Insets(40, 0, 0, 0);
            c.gridy++;
            c.gridx = 0;
            JLabel agrupadorLabel = new JLabel("Agrupador");
            camposConfigs.add(agrupadorLabel);
            add(agrupadorLabel, c);

            c.insets = new Insets(0, 0, 0, 0);
            c.gridy++;
            configsUtil.setAgrupador(new JComboBox<>());
            configsUtil.getAgrupador().addItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
            configsUtil.getAgrupador().setSelectedItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
            camposConfigs.add(configsUtil.getAgrupador());
            add(configsUtil.getAgrupador(), c);

            if(idCampo != null) {
                configsUtil.getAgrupador().setEnabled(false);
                insets = new Insets(-375, 0, 0, -800);
                c.gridy--;
                c.gridy--;
                carrregarConfigsCombobox(camposConfigs, idCampo);
            }
        }
        else if (tipoCampo.equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())) {
            insets = new Insets(-350, 0, 0, -800);
            c.insets = new Insets(40, 0, 0, 0);
            c.gridy++;
            c.gridx = 0;
            JLabel agrupadorLabel = new JLabel("Agrupador");
            camposConfigs.add(agrupadorLabel);
            add(agrupadorLabel, c);

            c.insets = new Insets(0, 0, 0, 0);
            c.gridy++;
            configsUtil.setAgrupador(new JComboBox<>());
            configsUtil.getAgrupador().addItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
            configsUtil.getAgrupador().setSelectedItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
            camposConfigs.add(configsUtil.getAgrupador());
            add(configsUtil.getAgrupador(), c);

            if(idCampo != null) {
                configsUtil.getAgrupador().setEnabled(false);
                insets = new Insets(-375, 0, 0, -800);
                c.gridy--;
                c.gridy--;
                carrregarConfigsRadio(camposConfigs, idCampo);
            }
        }
        else if (tipoCampo.equalsIgnoreCase(TipoCampoEnum.AGRUPADOR.getDescricao())) {
            if(idCampo != null) {
                insets = new Insets(-350, 0, 0, -800);
                carrregarConfigsAgrupador(camposConfigs);
            }
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
        ordem.removeAllItems();

        if (idCampo == null) {
            comboboxUtil.carregarOrdem(idCadastro, ordem, true);
            titulo = "Criar Campo";
            jButton.setText("Criar campo");
            jButton.removeActionListener(criarListener);
            criarListener = new CriarListener(this, idCadastro, idCampo, configsUtil);
            jButton.addActionListener(criarListener);
        } else {
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

    private void carrregarConfigsTexto(List<JComponent> camposConfigs, Integer idCampo){
        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        JLabel agrupadorLabel = new JLabel("Agrupador");
        camposConfigs.add(agrupadorLabel);
        add(agrupadorLabel, c);

        c.gridx = 3;
        JLabel valorPadraoLabel = new JLabel("Valor padrão");
        camposConfigs.add(valorPadraoLabel);
        add(valorPadraoLabel, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        configsUtil.setAgrupador(new JComboBox<>());
        configsUtil.getAgrupador().addItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
        configsUtil.getAgrupador().setSelectedItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
        camposConfigs.add(configsUtil.getAgrupador());
        add(configsUtil.getAgrupador(), c);

        c.gridx = 3;
        configsUtil.setValorPadrao(new JTextField());
        camposConfigs.add(configsUtil.getValorPadrao());
        configsUtil.getValorPadrao().setColumns(7);
        add(configsUtil.getValorPadrao(), c);

        if(idCampo != null){
            configsUtil.getAgrupador().setEnabled(false);
        }
    }

    private void carrregarConfigsNumerico(List<JComponent> camposConfigs, Integer idCampo){
        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        JLabel agrupadorLabel = new JLabel("Agrupador");
        camposConfigs.add(agrupadorLabel);
        add(agrupadorLabel, c);

        c.gridx = 2;
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
        configsUtil.setAgrupador(new JComboBox<>());
        configsUtil.getAgrupador().addItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
        configsUtil.getAgrupador().setSelectedItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
        camposConfigs.add(configsUtil.getAgrupador());
        add(configsUtil.getAgrupador(), c);

        c.gridx = 2;
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

        if(idCampo != null){
            configsUtil.getAgrupador().setEnabled(false);
        }
    }

    private void carrregarConfigsDataHora(List<JComponent> camposConfigs, Integer idCampo){
        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        JLabel agrupadorLabel = new JLabel("Agrupador");
        camposConfigs.add(agrupadorLabel);
        add(agrupadorLabel, c);

        c.gridx = 2;
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
        configsUtil.setAgrupador(new JComboBox<>());
        configsUtil.getAgrupador().addItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
        configsUtil.getAgrupador().setSelectedItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
        camposConfigs.add(configsUtil.getAgrupador());
        add(configsUtil.getAgrupador(), c);

        c.gridx = 2;
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

        if(idCampo != null){
            configsUtil.getAgrupador().setEnabled(false);
        }
    }

    private void carrregarConfigsData(List<JComponent> camposConfigs, Integer idCampo){
        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        JLabel agrupadorLabel = new JLabel("Agrupador");
        camposConfigs.add(agrupadorLabel);
        add(agrupadorLabel, c);

        c.gridx = 2;
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
        configsUtil.setAgrupador(new JComboBox<>());
        configsUtil.getAgrupador().addItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
        configsUtil.getAgrupador().setSelectedItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
        camposConfigs.add(configsUtil.getAgrupador());
        add(configsUtil.getAgrupador(), c);

        c.gridx = 2;
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

        if(idCampo != null){
            configsUtil.getAgrupador().setEnabled(false);
        }
    }

    private void carrregarConfigsCheckbox(List<JComponent> camposConfigs, Integer idCampo){
        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        JLabel agrupadorLabel = new JLabel("Agrupador");
        camposConfigs.add(agrupadorLabel);
        add(agrupadorLabel, c);

        c.gridx = 3;
        JLabel valorPadraoLabel = new JLabel("Estado padrão");
        camposConfigs.add(valorPadraoLabel);
        add(valorPadraoLabel, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        configsUtil.setAgrupador(new JComboBox<>());
        configsUtil.getAgrupador().addItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
        configsUtil.getAgrupador().setSelectedItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
        camposConfigs.add(configsUtil.getAgrupador());
        add(configsUtil.getAgrupador(), c);

        c.gridx = 3;
        configsUtil.setEstadoPadrao(new JCheckBox());
        camposConfigs.add(configsUtil.getEstadoPadrao());
        add(configsUtil.getEstadoPadrao(), c);

        if(idCampo != null){
            configsUtil.getAgrupador().setEnabled(false);
        }
    }

    private void carrregarConfigsAreaTexto(List<JComponent> camposConfigs, Integer idCampo){
        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        JLabel agrupadorLabel = new JLabel("Agrupador");
        camposConfigs.add(agrupadorLabel);
        add(agrupadorLabel, c);

        c.gridx = 2;
        JLabel limiteCaracteresLabel = new JLabel("Limite de caracteres");
        camposConfigs.add(limiteCaracteresLabel);
        add(limiteCaracteresLabel, c);

        c.gridx = 3;
        JLabel valorPadraoLabel = new JLabel("Valor padrão");
        camposConfigs.add(valorPadraoLabel);
        add(valorPadraoLabel, c);

        c.insets = new Insets(-40, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        configsUtil.setAgrupador(new JComboBox<>());
        configsUtil.getAgrupador().addItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
        configsUtil.getAgrupador().setSelectedItem(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao());
        camposConfigs.add(configsUtil.getAgrupador());
        add(configsUtil.getAgrupador(), c);

        c.insets = new Insets(-45, 70, 0, 0);
        c.gridx = 2;
        configsUtil.setLimiteCaracteres(new JTextField());
        configsUtil.getLimiteCaracteres().addKeyListener(new NumericoUtil(configsUtil.getLimiteCaracteres()));
        configsUtil.getLimiteCaracteres().setColumns(7);
        camposConfigs.add(configsUtil.getLimiteCaracteres());
        add(configsUtil.getLimiteCaracteres(), c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridx = 3;
        configsUtil.setValorPadraoArea(new JTextArea());
        camposConfigs.add(configsUtil.getValorPadraoArea());
        textAreaUtil.configurarTextArea(configsUtil.getValorPadraoArea());
        add(configsUtil.getValorPadraoArea(), c);

        if(idCampo != null){
            configsUtil.getAgrupador().setEnabled(false);
        }
    }

    private void carrregarConfigsCombobox(List<JComponent> camposConfigs, Integer idCampo){
        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        JLabel novaOpcaoLabel = new JLabel("Nova opção");
        camposConfigs.add(novaOpcaoLabel);
        add(novaOpcaoLabel, c);

        c.gridx = 2;
        JLabel opcoesAdicionadasLabel = new JLabel("Opções adicionadas");
        camposConfigs.add(opcoesAdicionadasLabel);
        add(opcoesAdicionadasLabel, c);

        c.gridx = 3;
        JLabel opcaoPadraoLabel = new JLabel("Opção padrão");
        camposConfigs.add(opcaoPadraoLabel);
        add(opcaoPadraoLabel, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        configsUtil.setNovaOpcao(new JTextField());
        configsUtil.getNovaOpcao().setColumns(7);
        camposConfigs.add(configsUtil.getNovaOpcao());
        add(configsUtil.getNovaOpcao(), c);

        c.gridx = 2;
        configsUtil.setOpcoesAdicionadas(new JComboBox<>());
        camposConfigs.add(configsUtil.getOpcoesAdicionadas());
        add(configsUtil.getOpcoesAdicionadas(), c);

        c.gridx = 3;
        configsUtil.setOpcaoPadrao(new JComboBox<>());
        camposConfigs.add(configsUtil.getOpcaoPadrao());
        add(configsUtil.getOpcaoPadrao(), c);

        c.gridy++;
        c.gridx = 1;
        configsUtil.setAdicionarOpcao(new JButton("Adicionar opção"));
        configsUtil.getAdicionarOpcao().setEnabled(false);
        camposConfigs.add(configsUtil.getAdicionarOpcao());
        add(configsUtil.getAdicionarOpcao(), c);

        c.gridx = 2;
        configsUtil.setRemoverOpcao(new JButton("Remover opção"));
        configsUtil.getRemoverOpcao().setEnabled(false);
        camposConfigs.add(configsUtil.getRemoverOpcao());
        add(configsUtil.getRemoverOpcao(), c);

        configsUtil.getAdicionarOpcao().addActionListener(new AdicionarOpcaoListener(idCampo, true, configsUtil.getOpcoesAdicionadas(), configsUtil.getOpcaoPadrao(), configsUtil.getNovaOpcao(), true));
        configsUtil.getRemoverOpcao().addActionListener(new RemoverOpcaoListener(idCampo, true, configsUtil.getOpcoesAdicionadas(), configsUtil.getOpcaoPadrao(), true));
    }

    private void carrregarConfigsRadio(List<JComponent> camposConfigs, Integer idCampo){
        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        JLabel novaOpcaoLabel = new JLabel("Nova opção");
        camposConfigs.add(novaOpcaoLabel);
        add(novaOpcaoLabel, c);

        c.gridx = 2;
        JLabel opcoesAdicionadasLabel = new JLabel("Opções adicionadas");
        camposConfigs.add(opcoesAdicionadasLabel);
        add(opcoesAdicionadasLabel, c);

        c.gridx = 3;
        JLabel opcaoPadraoLabel = new JLabel("Opção padrão");
        camposConfigs.add(opcaoPadraoLabel);
        add(opcaoPadraoLabel, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        configsUtil.setNovaOpcao(new JTextField());
        configsUtil.getNovaOpcao().setColumns(7);
        camposConfigs.add(configsUtil.getNovaOpcao());
        add(configsUtil.getNovaOpcao(), c);

        c.gridx = 2;
        configsUtil.setOpcoesAdicionadas(new JComboBox<>());
        camposConfigs.add(configsUtil.getOpcoesAdicionadas());
        add(configsUtil.getOpcoesAdicionadas(), c);

        c.gridx = 3;
        configsUtil.setOpcaoPadrao(new JComboBox<>());
        camposConfigs.add(configsUtil.getOpcaoPadrao());
        add(configsUtil.getOpcaoPadrao(), c);

        c.gridy++;
        c.gridx = 1;
        configsUtil.setAdicionarOpcao(new JButton("Adicionar opção"));
        configsUtil.getAdicionarOpcao().setEnabled(false);
        camposConfigs.add(configsUtil.getAdicionarOpcao());
        add(configsUtil.getAdicionarOpcao(), c);

        c.gridx = 2;
        configsUtil.setRemoverOpcao(new JButton("Remover opção"));
        configsUtil.getRemoverOpcao().setEnabled(false);
        camposConfigs.add(configsUtil.getRemoverOpcao());
        add(configsUtil.getRemoverOpcao(), c);

        configsUtil.getAdicionarOpcao().addActionListener(new AdicionarOpcaoListener(idCampo, true, configsUtil.getOpcoesAdicionadas(), configsUtil.getOpcaoPadrao(), configsUtil.getNovaOpcao(), false));
        configsUtil.getRemoverOpcao().addActionListener(new RemoverOpcaoListener(idCampo, true, configsUtil.getOpcoesAdicionadas(), configsUtil.getOpcaoPadrao(), false));
    }

    private void carrregarConfigsAgrupador(List<JComponent> camposConfigs){
        c.insets = new Insets(40, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        JLabel novaOpcaoLabel = new JLabel("Direcionar por campo");
        camposConfigs.add(novaOpcaoLabel);
        add(novaOpcaoLabel, c);

        c.gridx = 3;
        JLabel opcaoPadraoLabel = new JLabel("Mais recentes primeiro");
        camposConfigs.add(opcaoPadraoLabel);
        add(opcaoPadraoLabel, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridy++;
        c.gridx = 1;
        configsUtil.setCampoOrdenador(new JComboBox<>());
        camposConfigs.add(configsUtil.getCampoOrdenador());
        add(configsUtil.getCampoOrdenador(), c);

        c.gridx = 3;
        configsUtil.setOrdenacaoDesc(new JCheckBox());
        camposConfigs.add(configsUtil.getOrdenacaoDesc());
        add(configsUtil.getOrdenacaoDesc(), c);
    }

    private void carregarInfosCampo(Integer idCampo) throws DaoException{
        List<Map<String, Object>> infosConfigsList;
        List<Map<String, Object>> infosCampoList = daoUtil.select(String.format("SELECT ordem, label, coluna, tipo, nativo, agrupador FROM CAMPOSCADASTROS WHERE id = %d", idCampo), Arrays.asList("ordem", "label", "coluna", "tipo", "nativo", "agrupador"));

        ordem.setSelectedItem(infosCampoList.get(0).get("ordem").toString());
        label.setText(infosCampoList.get(0).get("label").toString());
        coluna.setText(infosCampoList.get(0).get("coluna").toString());
        tipo.setSelectedItem(infosCampoList.get(0).get("tipo").toString());

        if(configsUtil.getAgrupador() != null) {
            List<Map<String, Object>> agrupadores = daoUtil.select("SELECT label FROM CAMPOSCADASTROS WHERE tipo = 'Agrupador'", Collections.singletonList("label"));
            for (Map<String, Object> agrupador : agrupadores) {
                configsUtil.getAgrupador().addItem(agrupador.get("label").toString());
            }

            if (infosCampoList.get(0).get("agrupador") != null && !infosCampoList.get(0).get("agrupador").toString().equalsIgnoreCase("0")) {
                List<Map<String, Object>> agrupadorSelecionadoList = daoUtil.select(String.format("SELECT label FROM CAMPOSCADASTROS WHERE id = %d", Integer.parseInt(infosCampoList.get(0).get("agrupador").toString())), Collections.singletonList("label"));
                String agrupadorSelecionado = agrupadorSelecionadoList.get(0).toString();
                configsUtil.getAgrupador().setSelectedItem(agrupadorSelecionado);
            }
        }

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
        else if (infosCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())) {
            infosConfigsList = daoUtil.select(String.format("SELECT * FROM CONFIGSCAMPOSCHECKBOX WHERE idcampo = %d AND cadastro = true", idCampo), Collections.singletonList("estadopadrao"));

            if (infosConfigsList.get(0).get("estadopadrao") != null && infosConfigsList.get(0).get("estadopadrao").toString().equalsIgnoreCase("true")) {
                configsUtil.getEstadoPadrao().setSelected(true);
            }

            if (infosCampoList.get(0).get("nativo") != null) {
                configsUtil.getEstadoPadrao().setEnabled(false);
            }
        }
        else if (infosCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.AREATEXTO.getDescricao())) {
            infosConfigsList = daoUtil.select(String.format("SELECT * FROM CONFIGSCAMPOSAREATEXTO WHERE idcampo = %d AND cadastro = true", idCampo), Arrays.asList("valorpadrao", "limitecaracteres"));

            if (infosConfigsList.get(0).get("valorpadrao") != null) {
                configsUtil.getValorPadraoArea().setText(infosConfigsList.get(0).get("valorpadrao").toString());
            }

            if (infosConfigsList.get(0).get("limitecaracteres") != null) {
                configsUtil.getLimiteCaracteres().setText(infosConfigsList.get(0).get("limitecaracteres").toString());
            }

            if (infosCampoList.get(0).get("nativo") != null) {
                configsUtil.getValorPadraoArea().setEnabled(false);
                configsUtil.getLimiteCaracteres().setEnabled(false);
            }
        }
        else if (infosCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.COMBOBOX.getDescricao())) {
            configsUtil.getOpcoesAdicionadas().removeAllItems();
            configsUtil.getOpcaoPadrao().removeAllItems();

            configsUtil.getOpcoesAdicionadas().addItem(OpcaoComboEnum.OPCAO_COMBO_RADIO.getDescricao());
            configsUtil.getOpcoesAdicionadas().setSelectedItem(OpcaoComboEnum.OPCAO_COMBO_RADIO.getDescricao());
            configsUtil.getOpcaoPadrao().addItem(OpcaoComboEnum.SEM_OPCAO_PADRAO.getDescricao());
            configsUtil.getOpcaoPadrao().setSelectedItem(OpcaoComboEnum.SEM_OPCAO_PADRAO.getDescricao());

            infosConfigsList = daoUtil.select(String.format("SELECT * FROM CONFIGSCAMPOSCOMBOBOX WHERE idcampo = %d AND cadastro = true", idCampo), Arrays.asList("opcaopadrao", "opcoes"));

            if (infosConfigsList.get(0).get("opcoes") != null) {
                String[] opcoes = infosConfigsList.get(0).get("opcoes").toString().split("_");

                for(String opcao: opcoes){
                    if(opcao.length() > 0) {
                        configsUtil.getOpcoesAdicionadas().addItem(opcao);
                        configsUtil.getOpcaoPadrao().addItem(opcao);
                    }
                }

                if (infosConfigsList.get(0).get("opcaopadrao") != null) {
                    configsUtil.getOpcaoPadrao().setSelectedItem(infosConfigsList.get(0).get("opcaopadrao").toString());
                }
            }

            configsUtil.getAdicionarOpcao().setEnabled(true);
            configsUtil.getRemoverOpcao().setEnabled(true);

            if (infosCampoList.get(0).get("nativo") != null) {
                configsUtil.getNovaOpcao().setEnabled(false);
                configsUtil.getOpcoesAdicionadas().setEnabled(false);
                configsUtil.getOpcaoPadrao().setEnabled(false);
                configsUtil.getAdicionarOpcao().setEnabled(false);
                configsUtil.getRemoverOpcao().setEnabled(false);
            }
        }
        else if (infosCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())) {
            configsUtil.getOpcoesAdicionadas().removeAllItems();
            configsUtil.getOpcaoPadrao().removeAllItems();

            configsUtil.getOpcoesAdicionadas().addItem(OpcaoComboEnum.OPCAO_COMBO_RADIO.getDescricao());
            configsUtil.getOpcoesAdicionadas().setSelectedItem(OpcaoComboEnum.OPCAO_COMBO_RADIO.getDescricao());
            configsUtil.getOpcaoPadrao().addItem(OpcaoComboEnum.SEM_OPCAO_PADRAO.getDescricao());
            configsUtil.getOpcaoPadrao().setSelectedItem(OpcaoComboEnum.SEM_OPCAO_PADRAO.getDescricao());

            infosConfigsList = daoUtil.select(String.format("SELECT * FROM CONFIGSCAMPOSRADIO WHERE idcampo = %d AND cadastro = true", idCampo), Arrays.asList("opcaopadrao", "opcoes"));

            if (infosConfigsList.get(0).get("opcoes") != null) {
                String[] opcoes = infosConfigsList.get(0).get("opcoes").toString().split("_");

                for(String opcao: opcoes){
                    if(opcao.length() > 0) {
                        configsUtil.getOpcoesAdicionadas().addItem(opcao);
                        configsUtil.getOpcaoPadrao().addItem(opcao);
                    }
                }

                if (infosConfigsList.get(0).get("opcaopadrao") != null) {
                    configsUtil.getOpcaoPadrao().setSelectedItem(infosConfigsList.get(0).get("opcaopadrao").toString());
                }
            }

            configsUtil.getAdicionarOpcao().setEnabled(true);
            configsUtil.getRemoverOpcao().setEnabled(true);

            if (infosCampoList.get(0).get("nativo") != null) {
                configsUtil.getNovaOpcao().setEnabled(false);
                configsUtil.getOpcoesAdicionadas().setEnabled(false);
                configsUtil.getOpcaoPadrao().setEnabled(false);
                configsUtil.getAdicionarOpcao().setEnabled(false);
                configsUtil.getRemoverOpcao().setEnabled(false);
            }
        }
        else if (infosCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.AGRUPADOR.getDescricao())) {
            configsUtil.getCampoOrdenador().removeAllItems();
            configsUtil.getCampoOrdenador().addItem("ID");
            configsUtil.getCampoOrdenador().setSelectedItem("ID");

            List<Map<String, Object>> camposAgrupados = daoUtil.select(String.format("SELECT label FROM CAMPOSCADASTROS WHERE agrupador = %d", idCampo), Collections.singletonList("label"));
            for(Map<String, Object> campoAgrupado: camposAgrupados){
                configsUtil.getCampoOrdenador().addItem(campoAgrupado.get("label").toString());
            }
            infosConfigsList = daoUtil.select(String.format("SELECT * FROM CONFIGSCAMPOSAGRUPADOR WHERE idcampo = %d AND cadastro = true", idCampo), Arrays.asList("ordenacaodesc", "ordenacaocampo"));

            if(infosConfigsList.get(0).get("ordenacaodesc") != null && infosConfigsList.get(0).get("ordenacaodesc").toString().equalsIgnoreCase("true")) {
                configsUtil.getOrdenacaoDesc().setSelected(true);
            }

            if(infosConfigsList.get(0).get("ordenacaocampo") != null){
                configsUtil.getCampoOrdenador().setSelectedItem(infosConfigsList.get(0).get("ordenacaocampo"));
            }

            if (infosCampoList.get(0).get("nativo") != null) {
                configsUtil.getCampoOrdenador().setEnabled(false);
                configsUtil.getOrdenacaoDesc().setEnabled(false);
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
