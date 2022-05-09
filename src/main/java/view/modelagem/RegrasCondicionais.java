package view.modelagem;

import controle.DaoUtil;
import controle.JFrameUtil;
import controle.enums.OpcoesPadraoRegrasCondicionaisEnum;
import controle.enums.OperadoresRegrasCondicionaisEnum;
import controle.enums.TiposRegrasRegrasCondicionaisEnum;
import exception.DaoException;
import listener.home.VoltarListener;
import listener.modelagem.regrascondicionais.AdicionarListener;
import listener.modelagem.regrascondicionais.AlterarCampoListener;
import listener.modelagem.regrascondicionais.AtualizarListener;
import listener.modelagem.regrascondicionais.ExcluirListener;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class RegrasCondicionais extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final DaoUtil daoUtil = new DaoUtil();

    private final Integer idCampo;
    private final Integer idFormulario;
    private final boolean cadastro;

    private final JPanel jPanel = new JPanel();
    private final GridBagConstraints c = new GridBagConstraints();
    private final List<JComponent> campos = new ArrayList<>();

    public RegrasCondicionais(Integer idCampo, Integer idFormulario, boolean cadastro) {
        this.idCampo = idCampo;
        this.idFormulario = idFormulario;
        this.cadastro = cadastro;

        carregarCabecalho(jPanel);
        carregarRegras();

        JScrollPane scroll = new JScrollPane(jPanel);
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll);
        jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Regras condicionais", 1200, 700);
    }

    private void carregarCabecalho(JPanel jPanel) {
        jPanel.setLayout(new GridBagLayout());

        c.insets = new Insets(0, 20, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        jPanel.add(new JLabel("Tipo de regra"));

        c.insets = new Insets(0, 40, 0, 0);
        c.gridx++;
        jPanel.add(new JLabel("Campo"), c);

        c.insets = new Insets(0, 40, 0, 0);
        c.gridx++;
        jPanel.add(new JLabel("Operador"), c);

        c.gridx++;
        jPanel.add(new JLabel("Valor"), c);

        c.gridx++;
        jPanel.add(new JLabel("Grupo"), c);

        c.gridx++;
        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(new VoltarListener(this, idFormulario));
        jPanel.add(voltar, c);
    }

    public void carregarRegras() {
        for(JComponent campo: campos){
            jPanel.remove(campo);
        }
        campos.clear();

        try {
            List<Map<String, Object>> regras = daoUtil.select(String.format("SELECT * FROM REGRASCONDICIONAIS WHERE idcampo = %d AND cadastro = %s ORDER BY tiporegra, grupo", idCampo, cadastro), Arrays.asList("id", "operador", "valor", "idcampoinfo", "tiporegra", "grupo"));

            for (Map<String, Object> regra: regras) {
                c.insets = new Insets(10, 20, 0, 0);
                c.gridx = 0;
                c.gridy++;
                JComboBox<String> tipoRegra = new JComboBox<>();
                carregarOpcoesTiposRegras((String) regra.get("tiporegra"), tipoRegra);
                jPanel.add(tipoRegra, c);

                c.insets = new Insets(10, 40, 0, 0);
                c.gridx++;
                JComboBox<String> campo = new JComboBox<>();
                carregarOpcoesCampos(Integer.parseInt(regra.get("idcampoinfo").toString()), idFormulario, cadastro, campo);
                jPanel.add(campo, c);

                c.gridx++;
                JComboBox<String> operador = new JComboBox<>();
                carregarOpcoesOperadores((String) regra.get("operador"), operador);
                jPanel.add(operador, c);

                c.gridx++;
                JComboBox<String> valor = new JComboBox<>();
                campo.addActionListener(new AlterarCampoListener(cadastro, campo, valor, operador));
                valor.setSelectedItem(String.valueOf(regra.get("valor")));
                jPanel.add(valor, c);

                c.gridx++;
                JComboBox<String> grupo = new JComboBox<>();
                carregarOpcoesGrupos(String.valueOf(regra.get("grupo")), grupo);
                jPanel.add(grupo, c);

                c.gridx++;
                JButton atualizar = new JButton("Atualizar");
                atualizar.addActionListener(new AtualizarListener(this, Integer.parseInt(regra.get("id").toString()), idCampo, idFormulario, cadastro, tipoRegra, campo, operador, valor, grupo));
                jPanel.add(atualizar, c);

                c.gridx++;
                JButton excluir = new JButton("Excluir");
                excluir.addActionListener(new ExcluirListener(this, idCampo, idFormulario, Integer.parseInt(regra.get("id").toString()), cadastro));
                jPanel.add(excluir, c);

                campos.add(tipoRegra);
                campos.add(campo);
                campos.add(operador);
                campos.add(valor);
                campos.add(grupo);
                campos.add(atualizar);
                campos.add(excluir);
            }

            c.insets = new Insets(10, 20, 0, 0);
            c.gridx = 0;
            c.gridy++;
            JComboBox<String> tipoRegra = new JComboBox<>();
            carregarOpcoesTiposRegras(null, tipoRegra);
            jPanel.add(tipoRegra, c);

            c.insets = new Insets(10, 40, 0, 0);
            c.gridx++;
            JComboBox<String> campo = new JComboBox<>();
            carregarOpcoesCampos(null, idFormulario, cadastro, campo);
            jPanel.add(campo, c);

            c.gridx++;
            JComboBox<String> operador = new JComboBox<>();
            carregarOpcoesOperadores(null, operador);
            jPanel.add(operador, c);

            c.gridx++;
            JComboBox<String> valor = new JComboBox<>();
            campo.addActionListener(new AlterarCampoListener(cadastro, campo, valor, operador));
            jPanel.add(valor, c);

            c.gridx++;
            JComboBox<String> grupo = new JComboBox<>();
            carregarOpcoesGrupos(null, grupo);
            jPanel.add(grupo, c);

            c.gridx++;
            JButton adicionarRegra = new JButton("Adicionar Regra");
            adicionarRegra.addActionListener(new AdicionarListener(this, idCampo, idFormulario, tipoRegra, campo, operador, valor, grupo, cadastro));
            jPanel.add(adicionarRegra, c);

            campos.add(tipoRegra);
            campos.add(campo);
            campos.add(operador);
            campos.add(valor);
            campos.add(grupo);
            campos.add(adicionarRegra);
        }
        catch (DaoException exception) {
            JOptionPane.showMessageDialog(jPanel, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarOpcoesTiposRegras(String operacao, JComboBox<String> combobox) {
        combobox.addItem(OpcoesPadraoRegrasCondicionaisEnum.TIPOREGRA.getDescricao());
        combobox.setSelectedItem(OpcoesPadraoRegrasCondicionaisEnum.TIPOREGRA.getDescricao());
        for (TiposRegrasRegrasCondicionaisEnum tipoRegra: TiposRegrasRegrasCondicionaisEnum.values()) {
            combobox.addItem(tipoRegra.getDescricao());

            if (tipoRegra.getDescricao().equals(operacao)) {
                combobox.setSelectedItem(tipoRegra.getDescricao());
            }
        }
    }

    private void carregarOpcoesCampos(Integer idCampo, Integer idFormulario, boolean cadastro, JComboBox<String> combobox) throws DaoException {
        List<Map<String, Object>> campos = new ArrayList<>();
        if (cadastro) {
            campos = daoUtil.select(String.format("SELECT id, label FROM camposcadastros WHERE idcadastro = %d AND tipo IN ('Checkbox', 'Combobox', 'Rádio')", idFormulario), Arrays.asList("id", "label"));
        }

        combobox.addItem(OpcoesPadraoRegrasCondicionaisEnum.CAMPO.getDescricao());
        combobox.setSelectedItem(OpcoesPadraoRegrasCondicionaisEnum.CAMPO.getDescricao());
        for (Map<String, Object> campo: campos) {
            combobox.addItem((String) campo.get("label"));

            if (idCampo != null && Integer.parseInt(campo.get("id").toString()) == idCampo) {
                combobox.setSelectedItem(campo.get("label"));
            }
        }
    }

    private void carregarOpcoesOperadores(String operacao, JComboBox<String> combobox) {
        combobox.addItem(OpcoesPadraoRegrasCondicionaisEnum.OPERADOR.getDescricao());
        combobox.setSelectedItem(OpcoesPadraoRegrasCondicionaisEnum.OPERADOR.getDescricao());
        for (OperadoresRegrasCondicionaisEnum operador: OperadoresRegrasCondicionaisEnum.values()) {
            combobox.addItem(operador.getDescricao());

            if (operador.getDescricao().equals(operacao)) {
                combobox.setSelectedItem(operador.getDescricao());
            }
        }
    }

    private void carregarOpcoesGrupos(String grupo, JComboBox<String> combobox) throws DaoException {
        combobox.addItem(OpcoesPadraoRegrasCondicionaisEnum.GRUPO.getDescricao());
        combobox.setSelectedItem(OpcoesPadraoRegrasCondicionaisEnum.GRUPO.getDescricao());

        List<Map<String, Object>> gruposList = daoUtil.select(String.format("SELECT MAX(grupo) AS numgrupos FROM regrascondicionais WHERE idcampo = %d AND cadastro = %s", idCampo, cadastro), Collections.singletonList("numgrupos"));

        int numGrupos = 1;
        if (!gruposList.isEmpty() && gruposList.get(0).get("numgrupos") != null) {
            numGrupos = Integer.parseInt(gruposList.get(0).get("numgrupos").toString()) + 1;
        }

        for (int i = 1; i <= numGrupos; i++) {
            combobox.addItem(String.valueOf(i));

            if (String.valueOf(i).equals(grupo)) {
                combobox.setSelectedItem(String.valueOf(i));
            }
        }
    }
}
