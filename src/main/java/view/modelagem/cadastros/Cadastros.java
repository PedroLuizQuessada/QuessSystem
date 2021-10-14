package view.modelagem.cadastros;

import controle.DaoUtil;
import controle.JFrameUtil;
import controle.validacoes.CadastroUtil;
import controle.ComboboxUtil;
import exception.DaoException;
import listener.home.VoltarListener;
import listener.modelagem.cadastros.*;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class Cadastros extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final DaoUtil daoUtil = new DaoUtil();
    private final ComboboxUtil comboboxUtil = new ComboboxUtil();
    private final CadastroUtil cadastroUtil = new CadastroUtil();

    private final JPanel jPanel = new JPanel();
    private final GridBagConstraints c = new GridBagConstraints();
    private final List<JComponent> campos = new ArrayList<>();

    private final JTextField nomeFiltro = new JTextField();
    private final JComboBox<String> tipoPermissaoFiltro = new JComboBox<>();
    private final JTextField permitidosFiltro = new JTextField();

    public Cadastros(){
        carregarCabecalho(jPanel);
        carregarCadastros("SELECT id, nome, tabela, tipopermissao, permitidos FROM CADASTROS ORDER BY nome");

        JScrollPane scroll = new JScrollPane(jPanel);
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll);
        jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Cadastros", 1200, 700);
    }

    private void carregarCabecalho(JPanel jPanel){
        jPanel.setLayout(new GridBagLayout());

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        jPanel.add(new JLabel("Nome"), c);

        c.insets = new Insets(0, 50, 0, 0);
        c.gridx++;
        jPanel.add(new JLabel("Permissão"), c);

        c.gridx++;
        jPanel.add(new JLabel("Permitidos"), c);

        c.insets = new Insets(-40, 50, 0, 0);
        c.gridx++;
        c.gridx++;
        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(new VoltarListener(this));
        jPanel.add(voltar, c);

        c.insets = new Insets(0, 0, 30, 0);
        c.gridx = 0;
        c.gridy++;
        nomeFiltro.setColumns(7);
        jPanel.add(nomeFiltro, c);

        c.insets = new Insets(0, 50, 30, 0);
        c.gridx++;
        comboboxUtil.carregarTiposPermissao(tipoPermissaoFiltro);
        tipoPermissaoFiltro.addActionListener(new TipoPermissaoListener(tipoPermissaoFiltro, permitidosFiltro));
        jPanel.add(tipoPermissaoFiltro, c);

        c.gridx++;
        permitidosFiltro.setColumns(7);
        permitidosFiltro.setEnabled(false);
        jPanel.add(permitidosFiltro, c);

        c.gridx++;
        JButton botaoFiltro = new JButton("Buscar");
        botaoFiltro.addActionListener(new BuscarListener(this, nomeFiltro, tipoPermissaoFiltro, permitidosFiltro));
        jPanel.add(botaoFiltro, c);

        c.gridx++;
        JButton adicionarCadastro = new JButton("Adicionar cadastro");
        adicionarCadastro.addActionListener(new AdicionarListener(this));
        jPanel.add(adicionarCadastro, c);
    }

    public void carregarCadastros(String sql){
        for(JComponent campo: campos){
            jPanel.remove(campo);
        }
        campos.clear();

        try{
            List<Map<String, Object>> cadastros = daoUtil.select(sql, Arrays.asList("id", "nome", "tabela", "tipopermissao", "permitidos"));

            for(Map<String, Object> cadastro: cadastros){
                JTextField nomeCadastro = new JTextField();
                c.insets = new Insets(0, 0, 0, 0);
                c.gridx = 0;
                c.gridy++;
                nomeCadastro.setColumns(7);
                nomeCadastro.setEnabled(false);
                nomeCadastro.setText(cadastro.get("nome").toString());
                jPanel.add(nomeCadastro, c);

                c.insets = new Insets(0, 50, 0, 0);
                c.gridx++;
                JTextField tipoPermissaoCadastro = new JTextField();
                tipoPermissaoCadastro.setEnabled(false);
                tipoPermissaoCadastro.setColumns(7);
                tipoPermissaoCadastro.setText(cadastro.get("tipopermissao").toString());
                jPanel.add(tipoPermissaoCadastro, c);

                c.gridx++;
                JTextField permitidosCadastro = new JTextField();
                permitidosCadastro.setEnabled(false);
                permitidosCadastro.setColumns(7);
                permitidosCadastro.setText(cadastroUtil.carregaPermitidosNome(tipoPermissaoCadastro.getText(), Integer.parseInt(cadastro.get("permitidos").toString())));
                jPanel.add(permitidosCadastro, c);

                c.gridx++;
                JButton consultar = new JButton("Consultar");
                consultar.addActionListener(new ConsultarListener(this, Integer.valueOf(cadastro.get("id").toString())));
                jPanel.add(consultar, c);

                c.gridx++;
                JButton excluir = new JButton("Excluir");
                excluir.addActionListener(new ExcluirListener(this, Integer.valueOf(cadastro.get("id").toString()), String.valueOf(cadastro.get("tabela"))));
                jPanel.add(excluir, c);

                campos.add(nomeCadastro);
                campos.add(tipoPermissaoCadastro);
                campos.add(permitidosCadastro);
                campos.add(consultar);
                campos.add(excluir);
            }

            jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Cadastros", 1200, 700);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(jPanel, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
