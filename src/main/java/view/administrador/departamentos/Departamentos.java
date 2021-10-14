package view.administrador.departamentos;

import controle.DaoUtil;
import controle.JFrameUtil;
import exception.DaoException;
import listener.administrador.departamentos.*;
import listener.home.VoltarListener;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class Departamentos extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final DaoUtil daoUtil = new DaoUtil();

    private final JPanel jPanel = new JPanel();
    private final GridBagConstraints c = new GridBagConstraints();
    private final List<JComponent> campos = new ArrayList<>();

    private final JTextField nomeFiltro = new JTextField();
    private final JTextField gerenteFiltro = new JTextField();

    public Departamentos(){
        carregarCabecalho(jPanel);
        carregarDepartamentos("SELECT d.id AS deptoId, d.nome AS deptoNome, u.login AS usuLogin FROM DEPARTAMENTOS d LEFT JOIN USUARIOS u ON u.departamento = d.id WHERE u.gerente = true ORDER BY d.nome");

        JScrollPane scroll = new JScrollPane(jPanel);
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll);
        jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Departamentos", 1000, 700);
    }

    private void carregarCabecalho(JPanel jPanel){
        jPanel.setLayout(new GridBagLayout());

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        jPanel.add(new JLabel("Nome"), c);

        c.insets = new Insets(0, 50, 0, 0);
        c.gridx++;
        jPanel.add(new JLabel("Gerente"), c);

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
        gerenteFiltro.setColumns(7);
        jPanel.add(gerenteFiltro, c);

        c.gridx++;
        JButton botaoFiltro = new JButton("Buscar");
        botaoFiltro.addActionListener(new BuscarListener(this));
        jPanel.add(botaoFiltro, c);

        c.gridx++;
        JButton adicionarDepartamento = new JButton("Adicionar Departamento");
        adicionarDepartamento.addActionListener(new AdicionarListener(this));
        jPanel.add(adicionarDepartamento, c);
    }

    public void carregarDepartamentos(String sql){
        for(JComponent campo: campos){
            jPanel.remove(campo);
        }
        campos.clear();

        try {
            List<Map<String, Object>> departamentos = daoUtil.select(sql, Arrays.asList("deptoId", "deptoNome", "usuLogin"));

            for(Map<String, Object> departamento: departamentos){
                c.insets = new Insets(0, 0, 0, 0);
                c.gridx = 0;
                c.gridy++;
                JTextField nomeDepartamento = new JTextField();
                nomeDepartamento.setColumns(7);
                nomeDepartamento.setEnabled(false);
                nomeDepartamento.setText(departamento.get("deptoNome").toString());
                jPanel.add(nomeDepartamento, c);

                c.insets = new Insets(0, 50, 0, 0);
                c.gridx++;
                JTextField nomeGerente = new JTextField();
                nomeGerente.setColumns(7);
                nomeGerente.setEnabled(false);
                if(!String.valueOf(departamento.get("usuLogin")).equalsIgnoreCase("null")){
                    nomeGerente.setText(String.valueOf(departamento.get("usuLogin")));
                }
                else{
                    nomeGerente.setText("Sem gerente");
                }
                jPanel.add(nomeGerente, c);

                c.gridx++;
                JButton consultar = new JButton("Consultar");
                consultar.addActionListener(new ConsultarListener(this, Integer.parseInt(departamento.get("deptoId").toString())));
                jPanel.add(consultar, c);

                c.gridx++;
                JButton excluir = new JButton("Excluir");
                excluir.addActionListener(new ExcluirListener(this, Integer.parseInt(departamento.get("deptoId").toString())));
                jPanel.add(excluir, c);

                campos.add(nomeDepartamento);
                campos.add(nomeGerente);
                campos.add(consultar);
                campos.add(excluir);
            }

            jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Departamentos", 1000, 700);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(jPanel, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JTextField getNomeFiltro() {
        return nomeFiltro;
    }

    public JTextField getGerenteFiltro() {
        return gerenteFiltro;
    }
}
