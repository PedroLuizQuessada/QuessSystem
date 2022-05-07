package view.administrador.automacoes;

import controle.ComboboxUtil;
import controle.DaoUtil;
import controle.JFrameUtil;
import controle.PermissaoUtil;
import exception.DaoException;
import listener.administrador.automacoes.BuscarListener;
import listener.administrador.automacoes.ExcluirListener;
import listener.home.AutomacaoListener;
import listener.home.VoltarListener;
import main.Main;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class Automacoes extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final DaoUtil daoUtil = new DaoUtil();
    private final ComboboxUtil comboboxUtil = new ComboboxUtil();
    private final PermissaoUtil permissaoUtil = new PermissaoUtil();

    private final JPanel jPanel = new JPanel();
    private final GridBagConstraints c = new GridBagConstraints();
    private final List<JComponent> campos = new ArrayList<>();

    private final JTextField nomeFiltro = new JTextField();
    private final JComboBox<String> tipoPermissaoFiltro = new JComboBox<>();
    private final JTextField permitidosFiltro = new JTextField();
    private final JCheckBox ativoFiltro = new JCheckBox();

    public Automacoes() {
        carregarCabecalho(jPanel);
        carregarAutomacoes("SELECT id, nome, tipopermissao, permitidos, ativo FROM AUTOMACOES WHERE ativo = true ORDER BY nome");

        JScrollPane scroll = new JScrollPane(jPanel);
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll);
        jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Automações", 1000, 700);
    }

    private void carregarCabecalho(JPanel jPanel){
        jPanel.setLayout(new GridBagLayout());

        c.insets = new Insets(0, 20, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        jPanel.add(new JLabel("Nome"), c);

        c.insets = new Insets(0, 40, 0, 0);
        c.gridx++;
        jPanel.add(new JLabel("Permissão"), c);

        c.gridx++;
        jPanel.add(new JLabel("Permitidos"), c);

        c.gridx++;
        jPanel.add(new JLabel("Ativo"), c);

        c.insets = new Insets(0, 20, 30, 0);
        c.gridx = 0;
        c.gridy++;
        nomeFiltro.setColumns(7);
        jPanel.add(nomeFiltro, c);

        c.insets = new Insets(0, 40, 30, 0);
        c.gridx++;
        comboboxUtil.carregarTiposPermissao(tipoPermissaoFiltro);
        tipoPermissaoFiltro.addActionListener(new PermissaoUtil(tipoPermissaoFiltro, permitidosFiltro));
        jPanel.add(tipoPermissaoFiltro, c);

        c.gridx++;
        permitidosFiltro.setColumns(7);
        permitidosFiltro.setEnabled(false);
        jPanel.add(permitidosFiltro, c);

        c.gridx++;
        ativoFiltro.setSelected(true);
        jPanel.add(ativoFiltro, c);

        c.gridx++;
        JButton botaoFiltro = new JButton("Buscar");
        botaoFiltro.addActionListener(new BuscarListener(this, nomeFiltro, tipoPermissaoFiltro, permitidosFiltro, ativoFiltro));
        jPanel.add(botaoFiltro, c);

        c.gridx++;
        JButton adicionarAutomacao = new JButton("Adicionar automação");
        adicionarAutomacao.addActionListener(new AutomacaoListener(this, null));
        jPanel.add(adicionarAutomacao, c);

        c.gridx++;
        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(new VoltarListener(this));
        jPanel.add(voltar, c);
    }

    public void carregarAutomacoes(String sql) {
        for(JComponent campo: campos){
            jPanel.remove(campo);
        }
        campos.clear();

        try {
            List<Map<String, Object>> automacoes = daoUtil.select(sql, Arrays.asList("id", "nome", "tipopermissao", "permitidos", "ativo"));

            for (Map<String, Object> automacao: automacoes) {
                JTextField nomeAutomacao = new JTextField();
                c.insets = new Insets(0, 20, 0, 0);
                c.gridx = 0;
                c.gridy++;
                nomeAutomacao.setColumns(7);
                nomeAutomacao.setEnabled(false);
                nomeAutomacao.setText(automacao.get("nome").toString());
                jPanel.add(nomeAutomacao, c);

                c.insets = new Insets(0, 40, 0, 0);
                c.gridx++;
                JTextField tipoPermissaoAutomacao = new JTextField();
                tipoPermissaoAutomacao.setEnabled(false);
                tipoPermissaoAutomacao.setColumns(7);
                tipoPermissaoAutomacao.setText(automacao.get("tipopermissao").toString());
                jPanel.add(tipoPermissaoAutomacao, c);

                c.gridx++;
                JTextField permitidosAutomacao = new JTextField();
                permitidosAutomacao.setEnabled(false);
                permitidosAutomacao.setColumns(7);
                permitidosAutomacao.setText(permissaoUtil.carregaPermitidosNome(tipoPermissaoAutomacao.getText(), Integer.parseInt(automacao.get("permitidos").toString())));
                jPanel.add(permitidosAutomacao, c);

                c.gridx++;
                JCheckBox ativoAutomacao = new JCheckBox();
                ativoAutomacao.setEnabled(false);
                ativoAutomacao.setSelected(Boolean.parseBoolean(automacao.get("ativo").toString()));
                jPanel.add(ativoAutomacao, c);

                c.gridx++;
                JButton consultar = new JButton("Consultar");
                consultar.addActionListener(new AutomacaoListener(this, Integer.valueOf(automacao.get("id").toString())));
                jPanel.add(consultar, c);

                c.gridx++;
                JButton excluir = new JButton("Excluir");
                excluir.addActionListener(new ExcluirListener(this, Integer.valueOf(automacao.get("id").toString()), ativoFiltro));
                jPanel.add(excluir, c);

                campos.add(nomeAutomacao);
                campos.add(tipoPermissaoAutomacao);
                campos.add(permitidosAutomacao);
                campos.add(ativoAutomacao);
                campos.add(consultar);
                campos.add(excluir);
            }

            jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Automações", 1000, 700);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(jPanel, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
