package view.administrador.grupos;

import controle.DaoUtil;
import controle.JFrameUtil;
import exception.DaoException;
import listener.administrador.grupos.*;
import listener.home.VoltarListener;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class Grupos extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final DaoUtil daoUtil = new DaoUtil();

    private final JPanel jPanel = new JPanel();
    private final GridBagConstraints c = new GridBagConstraints();
    private final List<JComponent> campos = new ArrayList<>();

    private final JTextField nomeFiltro = new JTextField();
    private final JTextField liderFiltro = new JTextField();

    public Grupos(){
        carregarCabecalho(jPanel);
        carregarGrupos("SELECT g.id AS id, g.nome AS nome, u.login AS lider FROM GRUPOS g LEFT JOIN USUARIOS u ON u.id = g.lider ORDER BY g.nome");

        JScrollPane scroll = new JScrollPane(jPanel);
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll);
        jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Grupos", 1000, 700);
    }

    private void carregarCabecalho(JPanel jPanel){
        jPanel.setLayout(new GridBagLayout());

        c.insets = new Insets(0, 20, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        jPanel.add(new JLabel("Nome"), c);

        c.insets = new Insets(0, 40, 0, 0);
        c.gridx++;
        jPanel.add(new JLabel("Líder"), c);

        c.insets = new Insets(0, 20, 30, 0);
        c.gridx = 0;
        c.gridy++;
        nomeFiltro.setColumns(7);
        jPanel.add(nomeFiltro, c);

        c.insets = new Insets(0, 40, 30, 0);
        c.gridx++;
        liderFiltro.setColumns(7);
        jPanel.add(liderFiltro, c);

        c.gridx++;
        JButton botaoFiltro = new JButton("Buscar");
        botaoFiltro.addActionListener(new BuscarListener(this));
        jPanel.add(botaoFiltro, c);

        c.gridx++;
        JButton adicionarGrupo = new JButton("Adicionar Grupo");
        adicionarGrupo.addActionListener(new AdicionarListener(this));
        jPanel.add(adicionarGrupo, c);

        c.gridx++;
        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(new VoltarListener(this));
        jPanel.add(voltar, c);
    }

    public void carregarGrupos(String sql){
        for(JComponent campo: campos){
            jPanel.remove(campo);
        }
        campos.clear();

        try {
            List<Map<String, Object>> grupos = daoUtil.select(sql, Arrays.asList("id", "nome", "lider"));

            for(Map<String, Object> grupo: grupos){
                c.insets = new Insets(0, 20, 0, 0);
                c.gridx = 0;
                c.gridy++;
                JTextField nomeGrupo = new JTextField();
                nomeGrupo.setColumns(7);
                nomeGrupo.setEnabled(false);
                nomeGrupo.setText(grupo.get("nome").toString());
                jPanel.add(nomeGrupo, c);

                c.insets = new Insets(0, 40, 0, 0);
                c.gridx++;
                JTextField nomeLider = new JTextField();
                nomeLider.setColumns(7);
                nomeLider.setEnabled(false);
                if(!String.valueOf(grupo.get("lider")).equalsIgnoreCase("null")){
                    nomeLider.setText(String.valueOf(grupo.get("lider")));
                }
                else{
                    nomeLider.setText("Sem líder");
                }
                jPanel.add(nomeLider, c);

                c.gridx++;
                JButton consultar = new JButton("Consultar");
                consultar.addActionListener(new ConsultarListener(this, Integer.parseInt(grupo.get("id").toString())));
                jPanel.add(consultar, c);

                c.gridx++;
                JButton excluir = new JButton("Excluir");
                excluir.addActionListener(new ExcluirListener(this, Integer.parseInt(grupo.get("id").toString())));
                jPanel.add(excluir, c);

                campos.add(nomeGrupo);
                campos.add(nomeLider);
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

    public JTextField getLiderFiltro() {
        return liderFiltro;
    }
}
