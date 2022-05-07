package view.administrador.automacoes;

import controle.*;
import controle.enums.OpcaoComboEnum;
import controle.mascaras.HoraUtil;
import controle.mascaras.NumericoUtil;
import exception.DaoException;
import listener.administrador.automacoes.AlterarListener;
import listener.administrador.automacoes.CriarListener;
import listener.administrador.automacoes.DiasExecucaoListener;
import listener.administrador.automacoes.HorariosLimitesListener;
import listener.home.VoltarListener;
import main.Main;
import view.Home;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AdicionarConsultar extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final DaoUtil daoUtil = new DaoUtil();
    private final ComboboxUtil comboboxUtil = new ComboboxUtil();
    private final PermissaoUtil permissaoUtil = new PermissaoUtil();

    private final JTextField nome = new JTextField();
    private final JTextArea descricao = new JTextArea();
    private final JCheckBox horarioLimites = new JCheckBox();
    private final JTextField horarioLimiteInicio = new JTextField();
    private final JTextField horarioLimiteFim = new JTextField();
    private final JCheckBox maquinaExterna = new JCheckBox();
    private final JCheckBox diasExecucao = new JCheckBox();
    private final JCheckBox segunda = new JCheckBox();
    private final JCheckBox terca = new JCheckBox();
    private final JCheckBox quarta = new JCheckBox();
    private final JCheckBox quinta = new JCheckBox();
    private final JCheckBox sexta = new JCheckBox();
    private final JCheckBox sabado = new JCheckBox();
    private final JCheckBox domingo = new JCheckBox();
    private final JTextField emailFalha = new JTextField();
    private final JComboBox<String> tipoPermissao = new JComboBox<>();
    private final JComboBox<String> permitidos = new JComboBox<>();
    private final JCheckBox ativo = new JCheckBox();
    private final JButton jButton = new JButton("Criar automação");

    public AdicionarConsultar(Integer idAutomacao) {
        String titulo = "Adicionar Automação";
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Nome"), c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridx++;
        add(new JLabel("Descrição"), c);

        c.gridx++;
        add(new JLabel("E-mail para notificar falha"), c);

        c.gridx++;
        add(new JLabel("Ativo"), c);

        c.gridx++;
        add(new JLabel("Máquina externa"), c);

        c.insets = new Insets(-100, 70, 0, 0);
        c.gridx++;
        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(new VoltarListener(this));
        add(voltar, c);

        c.insets = new Insets(-45, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        nome.setColumns(7);
        add(nome, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridx++;
        AreaTextoUtil areaTextoUtil = new AreaTextoUtil(descricao);
        areaTextoUtil.configurarTextArea();
        add(descricao, c);

        c.insets = new Insets(-45, 70, 0, 0);
        c.gridx++;
        emailFalha.setColumns(7);
        add(emailFalha, c);

        c.gridx++;
        add(ativo, c);

        c.gridx++;
        add(maquinaExterna, c);

        c.insets = new Insets(50, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        add(new JLabel("Tipo de permissão"), c);

        c.insets = new Insets(50, 70, 0, 0);
        c.gridx++;
        add(new JLabel("Permitidos"), c);

        c.gridx++;
        add(new JLabel("Horários limites"), c);

        c.gridx++;
        JLabel horarioLimiteInicioLabel = new JLabel("Horário limite de início");
        horarioLimiteInicioLabel.setVisible(false);
        add(horarioLimiteInicioLabel, c);

        c.gridx++;
        JLabel horarioLimiteFimLabel = new JLabel("Horário limite de fim");
        horarioLimiteFimLabel.setVisible(false);
        add(horarioLimiteFimLabel, c);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        comboboxUtil.carregarTiposPermissao(tipoPermissao);
        tipoPermissao.addActionListener(new PermissaoUtil(tipoPermissao, permitidos));
        add(tipoPermissao, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridx++;
        permitidos.addItem(OpcaoComboEnum.PERMITIDOS.getDescricao());
        permitidos.setSelectedItem(OpcaoComboEnum.PERMITIDOS.getDescricao());
        add(permitidos, c);

        c.gridx++;
        horarioLimites.addActionListener(new HorariosLimitesListener(horarioLimites, horarioLimiteInicioLabel, horarioLimiteInicio, horarioLimiteFimLabel, horarioLimiteFim));
        add(horarioLimites, c);

        c.gridx++;
        horarioLimiteInicio.setColumns(7);
        horarioLimiteInicio.setVisible(false);
        horarioLimiteInicio.addKeyListener(new HoraUtil(horarioLimiteInicio));
        horarioLimiteInicio.addKeyListener(new NumericoUtil(horarioLimiteInicio));
        add(horarioLimiteInicio, c);

        c.gridx++;
        horarioLimiteFim.setColumns(7);
        horarioLimiteFim.setVisible(false);
        horarioLimiteFim.addKeyListener(new HoraUtil(horarioLimiteFim));
        horarioLimiteFim.addKeyListener(new NumericoUtil(horarioLimiteFim));
        add(horarioLimiteFim, c);

        c.insets = new Insets(40, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        add(new JLabel("Dias de execução"), c);

        c.insets = new Insets(40, 70, 0, 0);
        c.gridx++;
        JLabel segundaLabel = new JLabel("Segunda");
        segundaLabel.setVisible(false);
        add(segundaLabel, c);

        c.gridx++;
        JLabel tercaLabel = new JLabel("Terça");
        tercaLabel.setVisible(false);
        add(tercaLabel, c);

        c.gridx++;
        JLabel quartaLabel = new JLabel("Quarta");
        quartaLabel.setVisible(false);
        add(quartaLabel, c);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        add(diasExecucao, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridx++;
        segunda.setVisible(false);
        add(segunda, c);

        c.gridx++;
        terca.setVisible(false);
        add(terca, c);

        c.gridx++;
        quarta.setVisible(false);
        add(quarta, c);

        c.insets = new Insets(40, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        JLabel quintaLabel = new JLabel("Quinta");
        quintaLabel.setVisible(false);
        add(quintaLabel, c);

        c.insets = new Insets(40, 70, 0, 0);
        c.gridx++;
        JLabel sextaLabel = new JLabel("Sexta");
        sextaLabel.setVisible(false);
        add(sextaLabel, c);

        c.gridx++;
        JLabel sabadoLabel = new JLabel("Sábado");
        sabadoLabel.setVisible(false);
        add(sabadoLabel, c);

        c.gridx++;
        JLabel domingoLabel = new JLabel("Domingo");
        domingoLabel.setVisible(false);
        add(domingoLabel, c);
        diasExecucao.addActionListener(new DiasExecucaoListener(segundaLabel, tercaLabel, quartaLabel, quintaLabel, sextaLabel, sabadoLabel, domingoLabel, diasExecucao, segunda, terca, quarta, quinta, sexta, sabado, domingo));

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        quinta.setVisible(false);
        add(quinta, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridx++;
        sexta.setVisible(false);
        add(sexta, c);

        c.gridx++;
        sabado.setVisible(false);
        add(sabado, c);

        c.gridx++;
        domingo.setVisible(false);
        add(domingo, c);

        c.insets = new Insets(40, 70, 0, 0);
        c.gridx = 2;
        c.gridy++;
        add(jButton, c);

        try {
            if(idAutomacao != null) {
                titulo = "Consultar Automação";
                carregarInfosAutomacao(idAutomacao);
                jButton.setText("Alterar");
                jButton.addActionListener(new AlterarListener(this, idAutomacao));
            }
            else {
                jButton.addActionListener(new CriarListener(this));
            }

            jFrameUtil.configurarJanela(this, Main.getImageIcon(), titulo, 1300, 700);
        } catch (DaoException exception){
            dispose();
            Main.getJanelas().add(new Home());
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarInfosAutomacao(Integer idAutomacao) throws DaoException {
        List<Map<String, Object>> automacaoList = daoUtil.select(String.format("SELECT * FROM AUTOMACOES WHERE id = %d", idAutomacao), Arrays.asList("nome", "descricao", "horariolimites", "horariolimiteinicio", "horariolimitefim", "maquinaexterna", "diasexecucao", "segunda", "terca", "quarta", "quinta", "sexta", "sabado", "domingo", "emailfalha", "tipopermissao", "permitidos", "ativo"));
        if (!automacaoList.isEmpty()) {
            Map<String, Object> automacao = automacaoList.get(0);

            nome.setText(String.valueOf(automacao.get("nome")));
            descricao.setText(String.valueOf(automacao.get("descricao")));
            horarioLimites.setSelected(Boolean.parseBoolean(automacao.get("horariolimites").toString()));
            horarioLimiteInicio.setText(String.valueOf(automacao.get("horariolimiteinicio")));
            horarioLimiteFim.setText(String.valueOf(automacao.get("horariolimitefim")));
            maquinaExterna.setSelected(Boolean.parseBoolean(String.valueOf(automacao.get("maquinaexterna"))));
            diasExecucao.setSelected(Boolean.parseBoolean(String.valueOf(automacao.get("diasexecucao"))));
            segunda.setSelected(Boolean.parseBoolean(String.valueOf(automacao.get("segunda"))));
            terca.setSelected(Boolean.parseBoolean(String.valueOf(automacao.get("terca"))));
            quarta.setSelected(Boolean.parseBoolean(String.valueOf(automacao.get("quarta"))));
            quinta.setSelected(Boolean.parseBoolean(String.valueOf(automacao.get("quinta"))));
            sexta.setSelected(Boolean.parseBoolean(String.valueOf(automacao.get("sexta"))));
            sabado.setSelected(Boolean.parseBoolean(String.valueOf(automacao.get("sabado"))));
            domingo.setSelected(Boolean.parseBoolean(String.valueOf(automacao.get("domingo"))));
            emailFalha.setText(String.valueOf(automacao.get("emailfalha")));
            tipoPermissao.setSelectedItem(String.valueOf(automacao.get("tipopermissao")));
            permitidos.setSelectedItem(permissaoUtil.carregaPermitidosNome(tipoPermissao.getSelectedItem().toString(), Integer.parseInt(String.valueOf(automacao.get("permitidos")))));
            ativo.setSelected(Boolean.parseBoolean(String.valueOf(automacao.get("ativo"))));
        }
    }

    public JTextField getNome() {
        return nome;
    }

    public JTextArea getDescricao() {
        return descricao;
    }

    public JCheckBox getHorarioLimites() {
        return horarioLimites;
    }

    public JTextField getHorarioLimiteInicio() {
        return horarioLimiteInicio;
    }

    public JTextField getHorarioLimiteFim() {
        return horarioLimiteFim;
    }

    public JCheckBox getMaquinaExterna() {
        return maquinaExterna;
    }

    public JCheckBox getDiasExecucao() {
        return diasExecucao;
    }

    public JCheckBox getSegunda() {
        return segunda;
    }

    public JCheckBox getTerca() {
        return terca;
    }

    public JCheckBox getQuarta() {
        return quarta;
    }

    public JCheckBox getQuinta() {
        return quinta;
    }

    public JCheckBox getSexta() {
        return sexta;
    }

    public JCheckBox getSabado() {
        return sabado;
    }

    public JCheckBox getDomingo() {
        return domingo;
    }

    public JTextField getEmailFalha() {
        return emailFalha;
    }

    public JComboBox<String> getTipoPermissao() {
        return tipoPermissao;
    }

    public JComboBox<String> getPermitidos() {
        return permitidos;
    }

    public JCheckBox getAtivo() {
        return ativo;
    }
}
