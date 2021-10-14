package view.modelagem.cadastros.campos;

import controle.ComboboxUtil;
import controle.DaoUtil;
import controle.JFrameUtil;
import exception.DaoException;
import listener.home.VoltarListener;
import listener.modelagem.cadastros.campos.AdicionarListener;
import listener.modelagem.cadastros.campos.BuscarListener;
import listener.modelagem.cadastros.campos.ConsultarListener;
import listener.modelagem.cadastros.campos.ExcluirListener;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class Campos extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final ComboboxUtil comboboxUtil = new ComboboxUtil();
    private final DaoUtil daoUtil = new DaoUtil();

    private final JPanel jPanel = new JPanel();
    private final Integer idCadastro;
    private final GridBagConstraints c = new GridBagConstraints();
    private final List<JComponent> campos = new ArrayList<>();

    private final JTextField ordemFiltro = new JTextField();
    private final JTextField labelFiltro = new JTextField();
    private final JTextField colunaFiltro = new JTextField();
    private final JComboBox<String> tipoFiltro = new JComboBox<>();

    public Campos(Integer id){
        this.idCadastro = id;
        carregarCabecalho(jPanel, id);
        carregarCampos(String.format("SELECT id, idcadastro, ordem, label, coluna, tipo, nativo FROM CAMPOSCADASTROS WHERE idcadastro = %d AND inativo <> true ORDER BY ordem", id));

        JScrollPane scroll = new JScrollPane(jPanel);
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll);
        jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Campos", 1200, 700);
    }

    private void carregarCabecalho(JPanel jPanel, Integer id){
        jPanel.setLayout(new GridBagLayout());

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        jPanel.add(new JLabel("Ordem"), c);

        c.insets = new Insets(0, 50, 0, 0);
        c.gridx++;
        jPanel.add(new JLabel("Label"), c);

        c.gridx++;
        jPanel.add(new JLabel("Coluna"), c);

        c.gridx++;
        jPanel.add(new JLabel("Tipo"), c);

        c.insets = new Insets(-40, 50, 0, 0);
        c.gridx++;
        c.gridx++;
        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(new VoltarListener(this, id));
        jPanel.add(voltar, c);

        c.insets = new Insets(0, 0, 30, 0);
        c.gridy++;
        c.gridx = 0;
        ordemFiltro.setColumns(7);
        jPanel.add(ordemFiltro, c);

        c.insets = new Insets(0, 50, 30, 0);
        c.gridx++;
        labelFiltro.setColumns(7);
        jPanel.add(labelFiltro, c);

        c.gridx++;
        colunaFiltro.setColumns(7);
        jPanel.add(colunaFiltro, c);

        c.gridx++;
        comboboxUtil.carregarTiposCampos(tipoFiltro);
        jPanel.add(tipoFiltro, c);

        c.gridx++;
        JButton botaoFiltro = new JButton("Buscar");
        botaoFiltro.addActionListener(new BuscarListener(this, id));
        jPanel.add(botaoFiltro, c);

        c.gridx++;
        JButton adicionarCampo = new JButton("Adicionar campo");
        adicionarCampo.addActionListener(new AdicionarListener(this, id));
        jPanel.add(adicionarCampo, c);
    }

    public void carregarCampos(String sql){
        for(JComponent campo: campos){
            jPanel.remove(campo);
        }
        campos.clear();

        try{
            List<Map<String, Object>> camposList = daoUtil.select(sql, Arrays.asList("id", "idcadastro", "ordem", "label", "coluna", "tipo", "nativo"));

            for (Map<String, Object> campo: camposList){
                c.insets = new Insets(0, 0, 0, 0);
                c.gridy++;
                c.gridx = 0;
                JTextField ordemCampo = new JTextField();
                ordemCampo.setColumns(7);
                ordemCampo.setEnabled(false);
                ordemCampo.setText(campo.get("ordem").toString());
                jPanel.add(ordemCampo, c);

                c.insets = new Insets(0, 50, 0, 0);
                c.gridx++;
                JTextField labelCampo = new JTextField();
                labelCampo.setColumns(7);
                labelCampo.setEnabled(false);
                labelCampo.setText(campo.get("label").toString());
                jPanel.add(labelCampo, c);

                c.gridx++;
                JTextField colunaCampo = new JTextField();
                colunaCampo.setColumns(7);
                colunaCampo.setEnabled(false);
                colunaCampo.setText(campo.get("coluna").toString());
                jPanel.add(colunaCampo, c);

                c.gridx++;
                JTextField tipoCampo = new JTextField();
                tipoCampo.setColumns(7);
                tipoCampo.setEnabled(false);
                tipoCampo.setText(campo.get("tipo").toString());
                jPanel.add(tipoCampo, c);

                c.gridx++;
                JButton consultar = new JButton("Consultar");
                consultar.addActionListener(new ConsultarListener(this, idCadastro, Integer.valueOf(campo.get("id").toString())));
                jPanel.add(consultar, c);

                c.gridx++;
                JButton excluir = new JButton("Excluir");
                excluir.addActionListener(new ExcluirListener(this, Integer.valueOf(campo.get("idcadastro").toString()), Integer.valueOf(campo.get("id").toString()), Integer.valueOf(campo.get("ordem").toString())));
                jPanel.add(excluir, c);

                if(String.valueOf(campo.get("nativo")).equalsIgnoreCase("true")){
                    excluir.setEnabled(false);
                }

                campos.add(ordemCampo);
                campos.add(labelCampo);
                campos.add(colunaCampo);
                campos.add(tipoCampo);
                campos.add(consultar);
                campos.add(excluir);
            }

            jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Campos", 1200, 700);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(jPanel, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JTextField getOrdemFiltro() {
        return ordemFiltro;
    }

    public JTextField getLabelFiltro() {
        return labelFiltro;
    }

    public JTextField getColunaFiltro() {
        return colunaFiltro;
    }

    public JComboBox<String> getTipoFiltro() {
        return tipoFiltro;
    }
}
