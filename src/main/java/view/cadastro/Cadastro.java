package view.cadastro;

import controle.ComboboxUtil;
import controle.DaoUtil;
import controle.JFrameUtil;
import controle.enums.TipoCampoEnum;
import controle.mascaras.DataHoraUtil;
import controle.mascaras.DataUtil;
import controle.mascaras.NumericoUtil;
import exception.DaoException;
import listener.cadastro.BuscarListener;
import listener.cadastro.ExcluirListener;
import listener.home.VoltarListener;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Cadastro extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final DaoUtil daoUtil = new DaoUtil();
    private final ComboboxUtil comboboxUtil = new ComboboxUtil();

    private final JPanel jPanel = new JPanel();
    private final GridBagConstraints c = new GridBagConstraints();
    private final List<JComponent> campos = new ArrayList<>();
    private final List<Map<String, JComponent>> camposPesquisaveis = new ArrayList<>();

    private final int idCadastro;
    private final String nomeCadastro;

    public Cadastro(int idCadastro, String nomeCadastro){
        this.idCadastro = idCadastro;
        this.nomeCadastro = nomeCadastro;

        carregarCabecalho(jPanel);
        carregarRegistros(String.format("SELECT * FROM %s order BY id", nomeCadastro));

        JScrollPane scroll = new JScrollPane(jPanel);
        add(scroll);
        jFrameUtil.configurarJanela(this, Main.getImageIcon(), nomeCadastro, 1200, 700);
    }

    private void carregarCabecalho(JPanel jPanel) {
        try {
            List<Map<String, Object>> camposPesquisaveis = daoUtil.select(String.format("SELECT id, label, coluna, tipo FROM CAMPOSCADASTROS WHERE pesquisavel = true AND idcadastro = %d", idCadastro), Arrays.asList("id", "label", "coluna", "tipo"));
            jPanel.setLayout(new GridBagLayout());

            for (int i = 0; i < camposPesquisaveis.size(); i++){
                if(i == 0){
                    c.insets = new Insets(0, 20, 0, 0);
                    c.gridx = 0;
                    c.gridy = 0;
                }
                else {
                    c.insets = new Insets(0, 40, 0, 0);
                    c.gridx++;
                }

                jPanel.add(new JLabel(camposPesquisaveis.get(i).get("label").toString()), c);
            }

            for (int i = 0; i < camposPesquisaveis.size(); i++){
                Map<String, JComponent> mapCampoPesquisavel = new HashMap<>();
                JComponent filtro;

                if(camposPesquisaveis.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.COMBOBOX.getDescricao()) || camposPesquisaveis.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())){
                    JComboBox<String> filtroCombobox = new JComboBox<>();
                    comboboxUtil.carregarInfosCombobox(Integer.parseInt(camposPesquisaveis.get(i).get("id").toString()), filtroCombobox);
                    filtro = filtroCombobox;
                }
                else if(camposPesquisaveis.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())){
                    filtro = new JCheckBox();
                }
                else {
                    JTextField filtroTexto = new JTextField();
                    filtroTexto.setColumns(7);

                    if(camposPesquisaveis.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())){
                        filtroTexto.addKeyListener(new NumericoUtil(filtroTexto));
                    }
                    else if(camposPesquisaveis.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())){
                        filtroTexto.addKeyListener(new DataUtil(filtroTexto));
                    }
                    else if(camposPesquisaveis.get(i).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())){
                        filtroTexto.addKeyListener(new DataHoraUtil(filtroTexto));
                    }

                    filtro = filtroTexto;
                }

                mapCampoPesquisavel.put(camposPesquisaveis.get(i).get("coluna").toString(), filtro);
                this.camposPesquisaveis.add(mapCampoPesquisavel);

                if(i == 0){
                    c.insets = new Insets(0, 20, 30, 0);
                    c.gridx = 0;
                    c.gridy++;
                }
                else {
                    c.insets = new Insets(0, 40, 30, 0);
                    c.gridx++;
                }

                jPanel.add(filtro, c);
            }

            c.gridx++;
            JButton botaoFiltro = new JButton("Buscar");
            botaoFiltro.addActionListener(new BuscarListener(this, this.camposPesquisaveis, idCadastro));
            jPanel.add(botaoFiltro, c);

            c.gridx++;
            JButton adicionarRegistro = new JButton("Adicionar registro");
            //adicionarRegistro.addActionListener();
            jPanel.add(adicionarRegistro, c);

            c.gridx++;
            JButton voltar = new JButton("Voltar");
            voltar.addActionListener(new VoltarListener(this));
            jPanel.add(voltar, c);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(jPanel, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void carregarRegistros(String sql){
        for(JComponent campo: campos){
            jPanel.remove(campo);
        }
        campos.clear();

        try{
            List<String> camposPesquisaveis = new ArrayList<>();
            for (Map<String, JComponent> campoPesquisavel: this.camposPesquisaveis){
                for(Map.Entry<String, JComponent> valores: campoPesquisavel.entrySet()){
                    camposPesquisaveis.add(valores.getKey());
                }
            }

            List<Map<String, Object>> registros = daoUtil.select(sql, camposPesquisaveis);

            for(Map<String, Object> registro: registros){
                for (int i = 0; i < camposPesquisaveis.size(); i++){
                    if(i == 0){
                        c.insets = new Insets(0, 20, 0, 0);
                        c.gridx = 0;
                        c.gridy++;
                    }
                    else {
                        c.insets = new Insets(0, 40, 0, 0);
                        c.gridx++;
                    }

                    JComponent campo;
                    List<Map<String, Object>> tipoCampoList = daoUtil.select(String.format("SELECT tipo FROM CAMPOSCADASTROS WHERE idcadastro = %d AND coluna = '%s'", idCadastro, camposPesquisaveis.get(i)), Collections.singletonList("tipo"));
                    if(tipoCampoList.get(0).get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())){
                        JCheckBox campoCheckbox = new JCheckBox();
                        campoCheckbox.setSelected(registro.get(camposPesquisaveis.get(i)).toString().equalsIgnoreCase("true"));
                        campo = campoCheckbox;
                    }
                    else {
                        JTextField campoTexto = new JTextField();
                        campoTexto.setColumns(7);
                        campoTexto.setText(registro.get(camposPesquisaveis.get(i)).toString());
                        campo = campoTexto;
                    }


                    c.insets = new Insets(0, 20, 0, 0);
                    c.gridx = 0;
                    c.gridy++;
                    campo.setEnabled(false);
                    jPanel.add(campo, c);

                    campos.add(campo);
                }

                c.gridx++;
                JButton consultar = new JButton("Consultar");
                //consultar.addActionListener();
                jPanel.add(consultar, c);

                c.gridx++;
                JButton excluir = new JButton("Excluir");
                excluir.addActionListener(new ExcluirListener(idCadastro, nomeCadastro, Integer.parseInt(registro.get("id").toString())));
                jPanel.add(excluir, c);
            }

            jFrameUtil.configurarJanela(this, Main.getImageIcon(), nomeCadastro, 1200, 700);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(jPanel, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getNomeCadastro() {
        return nomeCadastro;
    }
}
