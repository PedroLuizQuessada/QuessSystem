package view.administrador.usuarios;

import controle.DaoUtil;
import controle.JFrameUtil;
import exception.DaoException;
import listener.administrador.usuarios.*;
import listener.home.VoltarListener;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

public class Usuarios extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final DaoUtil daoUtil = new DaoUtil();

    private final JPanel jPanel = new JPanel();
    private final GridBagConstraints c = new GridBagConstraints();
    private final List<JComponent> campos = new ArrayList<>();

    private final JTextField loginFiltro = new JTextField();
    private final JTextField emailFiltro = new JTextField();
    private final JTextField departamentoFiltro = new JTextField();
    private final JCheckBox gerenteFiltro = new JCheckBox();
    private final JCheckBox admFiltro = new JCheckBox();

    public Usuarios() {
        carregarCabecalho(jPanel);
        carregarUsuarios("SELECT u.id AS id, u.login AS login, u.email AS email, u.tentativasAcesso AS tentativasAcesso, u.adm AS adm, u.gerente AS gerente, u.nativo AS nativo, d.nome AS nomeDepto FROM USUARIOS u INNER JOIN DEPARTAMENTOS d ON u.departamento = d.id ORDER BY login");

        JScrollPane scroll = new JScrollPane(jPanel);
        scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll);
        jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Usuários", 1200, 700);
    }

    private void carregarCabecalho(JPanel jPanel) {
        jPanel.setLayout(new GridBagLayout());

        c.insets = new Insets(0, 20, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        jPanel.add(new JLabel("Login"), c);

        c.insets = new Insets(0, 40, 0, 0);
        c.gridx++;
        jPanel.add(new JLabel("E-mail"), c);

        c.gridx++;
        jPanel.add(new JLabel("Departamento"), c);

        c.gridx++;
        jPanel.add(new JLabel("Gerente"), c);

        c.gridx++;
        jPanel.add(new JLabel("Administrador"), c);

        c.insets = new Insets(0, 20, 30, 0);
        c.gridx = 0;
        c.gridy++;
        loginFiltro.setColumns(7);
        jPanel.add(loginFiltro, c);

        c.insets = new Insets(0, 40, 30, 0);
        c.gridx++;
        emailFiltro.setColumns(7);
        jPanel.add(emailFiltro, c);

        c.gridx++;
        departamentoFiltro.setColumns(7);
        jPanel.add(departamentoFiltro, c);

        c.gridx++;
        jPanel.add(gerenteFiltro, c);

        c.gridx++;
        jPanel.add(admFiltro, c);

        c.gridx++;
        JButton botaoFiltro = new JButton("Buscar");
        botaoFiltro.addActionListener(new BuscarListener(this));
        jPanel.add(botaoFiltro, c);

        c.gridx++;
        JButton adicionarUsuario = new JButton("Adicionar usuário");
        adicionarUsuario.addActionListener(new AdicionarListener(this));
        jPanel.add(adicionarUsuario, c);

        c.gridx++;
        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(new VoltarListener(this));
        jPanel.add(voltar, c);
    }

    public void carregarUsuarios(String sql){
        for(JComponent campo: campos){
            jPanel.remove(campo);
        }
        campos.clear();

        try {
            List<Map<String, Object>> usuarios = daoUtil.select(sql, Arrays.asList("id", "login", "email", "tentativasAcesso", "adm", "gerente", "nativo", "nomeDepto"));

            for (Map<String, Object> usuario : usuarios) {
                c.insets = new Insets(0, 20, 0, 0);
                c.gridx = 0;
                c.gridy++;
                JTextField loginUsuario = new JTextField();
                loginUsuario.setEnabled(false);
                loginUsuario.setColumns(7);
                loginUsuario.setText(usuario.get("login").toString());
                jPanel.add(loginUsuario, c);

                c.insets = new Insets(0, 40, 0, 0);
                c.gridx++;
                JTextField emailUsuario = new JTextField();
                emailUsuario.setEnabled(false);
                emailUsuario.setColumns(7);
                emailUsuario.setText(usuario.get("email").toString());
                jPanel.add(emailUsuario, c);

                c.gridx++;
                JTextField departamentoUsuario = new JTextField();
                departamentoUsuario.setEnabled(false);
                departamentoUsuario.setText(usuario.get("nomeDepto").toString());
                jPanel.add(departamentoUsuario, c);

                c.gridx++;
                JCheckBox gerenteUsuario = new JCheckBox();
                gerenteUsuario.setEnabled(false);
                gerenteUsuario.setSelected(Boolean.parseBoolean(String.valueOf(usuario.get("gerente"))));
                jPanel.add(gerenteUsuario, c);

                c.gridx++;
                JCheckBox admUsuario = new JCheckBox();
                admUsuario.setEnabled(false);
                admUsuario.setSelected(Boolean.parseBoolean(String.valueOf(usuario.get("adm"))));
                jPanel.add(admUsuario, c);

                c.gridx++;
                JButton consultar = new JButton("Consultar");
                consultar.addActionListener(new ConsultarListener(this, Integer.valueOf(usuario.get("id").toString())));
                jPanel.add(consultar, c);

                c.gridx++;
                JButton bloquear = new JButton("Bloquear");
                if (Integer.parseInt(usuario.get("tentativasAcesso").toString()) == 3) {
                    bloquear.setText("Desbloquear");
                }
                bloquear.addActionListener(new BloquearListener(this, bloquear, Integer.valueOf(usuario.get("id").toString())));
                jPanel.add(bloquear, c);

                c.gridx++;
                JButton excluir = new JButton("Excluir");
                excluir.addActionListener(new ExcluirListener(this, Integer.valueOf(usuario.get("id").toString())));
                jPanel.add(excluir, c);

                if (String.valueOf(usuario.get("nativo")).equalsIgnoreCase("true")) {
                    bloquear.setEnabled(false);
                    excluir.setEnabled(false);
                }

                campos.add(loginUsuario);
                campos.add(emailUsuario);
                campos.add(departamentoUsuario);
                campos.add(gerenteUsuario);
                campos.add(admUsuario);
                campos.add(consultar);
                campos.add(bloquear);
                campos.add(excluir);
            }

            jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Usuários", 1200, 700);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(jPanel, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JTextField getLoginFiltro() {
        return loginFiltro;
    }

    public JTextField getEmailFiltro() {
        return emailFiltro;
    }

    public JTextField getDepartamentoFiltro() {
        return departamentoFiltro;
    }

    public JCheckBox getGerenteFiltro() {
        return gerenteFiltro;
    }

    public JCheckBox getAdmFiltro() {
        return admFiltro;
    }
}
