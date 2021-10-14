package view.modelagem.cadastros;

import controle.DaoUtil;
import controle.JFrameUtil;
import controle.enums.OpcaoComboEnum;
import controle.validacoes.CadastroUtil;
import controle.ComboboxUtil;
import exception.DaoException;
import listener.home.VoltarListener;
import listener.modelagem.cadastros.*;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AdicionarConsultar extends JFrame {
    private final DaoUtil daoUtil = new DaoUtil();
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final ComboboxUtil comboboxUtil = new ComboboxUtil();
    private final CadastroUtil cadastroUtil = new CadastroUtil();

    private final JTextField nome = new JTextField();
    private final JTextField tabela = new JTextField();
    private final JComboBox<String> tipoPermissao = new JComboBox<>();
    private final JComboBox<String> permitidos = new JComboBox<>();
    private final JButton jButton = new JButton("Alterar");
    private final JButton limparTabela = new JButton("Limpar tabela");
    private final JButton acessarCampos = new JButton("Acessar campos");

    public AdicionarConsultar(Integer id){
        String titulo = "Consultar Cadastro";
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Nome"), c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridx++;
        add(new JLabel("Permissão"), c);

        c.gridx++;
        add(new JLabel("Permitidos"), c);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        nome.setColumns(7);
        add(nome, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridx++;
        comboboxUtil.carregarTiposPermissao(tipoPermissao);
        tipoPermissao.addActionListener(new TipoPermissaoListener(tipoPermissao, permitidos));
        add(tipoPermissao, c);

        c.gridx++;
        permitidos.addItem(OpcaoComboEnum.PERMITIDOS.getDescricao());
        permitidos.setSelectedItem(OpcaoComboEnum.PERMITIDOS.getDescricao());
        add(permitidos, c);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        add(new JLabel("Tabela"), c);

        c.insets = new Insets(40, 70, 0, 0);
        c.gridx++;
        limparTabela.addActionListener(new LimparTabelaListener(tabela));
        add(limparTabela, c);

        c.gridx++;
        acessarCampos.addActionListener(new AcessarCamposListener(this, id));
        add(acessarCampos, c);

        c.insets = new Insets(40, 0, 0, 0);
        c.gridx = 0;
        tabela.setColumns(7);
        add(tabela, c);

        c.insets = new Insets(40, 70, 0, 0);
        c.gridx = 1;
        c.gridy++;
        add(jButton, c);

        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(new VoltarListener(this));
        c.insets = new Insets(-300, 0, 0, -850);
        add(voltar, c);

        try {
            if (id == null) {
                titulo = "Criar Cadastro";

                limparTabela.setEnabled(false);
                acessarCampos.setEnabled(false);
                jButton.addActionListener(new CriarListener(this));
                jButton.setText("Criar cadastro");
            } else {
                tabela.setEnabled(false);
                jButton.addActionListener(new AlterarListener(id, nome, tipoPermissao, permitidos));
                carregarInfosCadastro(id);
            }

            jFrameUtil.configurarJanela(this, Main.getImageIcon(), titulo, 1000, 300);
        }
        catch (DaoException exception){
            dispose();
            Main.getJanelas().add(new Cadastros());
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarInfosCadastro(Integer id) throws DaoException {
        List<Map<String, Object>> infosCadastroList = daoUtil.select(String.format("SELECT nome, tabela, tipopermissao, permitidos FROM CADASTROS WHERE id = %d", id), Arrays.asList("nome", "tabela", "tipopermissao", "permitidos"));
        Map<String, Object> infosCadastro = infosCadastroList.get(0);

        nome.setText(String.valueOf(infosCadastro.get("nome")));
        tabela.setText(String.valueOf(infosCadastro.get("tabela")));
        tipoPermissao.setSelectedItem(String.valueOf(infosCadastro.get("tipopermissao")));
        permitidos.setSelectedItem(cadastroUtil.carregaPermitidosNome(tipoPermissao.getSelectedItem().toString(), Integer.parseInt(String.valueOf(infosCadastro.get("permitidos")))));
    }

    public JTextField getNome() {
        return nome;
    }

    public JTextField getTabela() {
        return tabela;
    }

    public JComboBox<String> getTipoPermissao() {
        return tipoPermissao;
    }

    public JComboBox<String> getPermitidos() {
        return permitidos;
    }
}
