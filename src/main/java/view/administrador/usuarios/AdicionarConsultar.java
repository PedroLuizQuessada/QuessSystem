package view.administrador.usuarios;

import controle.DaoUtil;
import controle.JFrameUtil;
import controle.SenhaUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;
import listener.home.VoltarListener;
import listener.administrador.usuarios.AlterarListener;
import listener.administrador.usuarios.CriarListener;
import listener.administrador.usuarios.IncluirUsuarioListener;
import listener.administrador.usuarios.RetirarUsuarioListener;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AdicionarConsultar extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final DaoUtil daoUtil = new DaoUtil();
    private final SenhaUtil senhaUtil = new SenhaUtil();

    private final JTextField login = new JTextField();
    private final JPasswordField senha = new JPasswordField();
    private final JTextField email = new JTextField();
    private final JCheckBox adm = new JCheckBox();
    private final JCheckBox gerente = new JCheckBox();
    private final JComboBox<String> departamento = new JComboBox<>();
    private final JComboBox<String> gruposIncluidos = new JComboBox<>();
    private final JComboBox<String> gruposExcluidos = new JComboBox<>();
    private final JButton retirarUsuario = new JButton("Retirar usuário do grupo");
    private final JButton incluirUsuario = new JButton("Incluir usuário no grupo");
    private final JButton jButton = new JButton("Criar usuário");

    public AdicionarConsultar(Integer id){
        String titulo = "Adicionar Usuário";
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Login"), c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridx++;
        add(new JLabel("Senha"), c);

        c.gridx++;
        add(new JLabel("E-mail"), c);

        c.gridx++;
        add(new JLabel("Administrador"), c);

        c.gridx++;
        add(new JLabel("Gerente"), c);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        login.setColumns(7);
        add(login, c);

        c.insets = new Insets(0, 70, 0, 0);
        c.gridx++;
        senha.setColumns(7);
        add(senha, c);

        c.gridx++;
        email.setColumns(7);
        add(email, c);

        c.gridx++;
        add(adm, c);

        c.gridx++;
        add(gerente, c);

        c.gridy++;

        try{
            carregarDepartamentos();

            if(id != null){
                titulo = "Consultar Usuário";
                carregarInfosUsuario(id);
                jButton.addActionListener(new AlterarListener(this, id));

                c.insets = new Insets(40, 70, 0, 0);
                c.gridx = 1;
                add(new JLabel("Grupos ao qual pertence"), c);

                c.gridx = 3;
                add(new JLabel("Grupos que não participa"), c);

                c.insets = new Insets(0, 70, 0, 0);
                c.gridx = 1;
                c.gridy++;
                add(gruposIncluidos, c);

                c.gridx = 3;
                add(gruposExcluidos, c);

                c.insets = new Insets(-30, 70, 0, 0);
                c.gridx = 1;
                c.gridy++;
                retirarUsuario.addActionListener(new RetirarUsuarioListener(this, id));
                add(retirarUsuario, c);

                c.gridx = 3;
                incluirUsuario.addActionListener(new IncluirUsuarioListener(this, id));
                add(incluirUsuario, c);

                c.gridy--;
                c.gridy--;
            }
            else{
                departamento.insertItemAt(OpcaoComboEnum.DEPARTAMENTO.getDescricao(), 0);
                departamento.setSelectedItem(OpcaoComboEnum.DEPARTAMENTO.getDescricao());
                jButton.addActionListener(new CriarListener(this));
            }

            c.insets = new Insets(40, 70, 0, 0);
            c.gridx = 2;
            add(new JLabel("Departamento"), c);

            c.insets = new Insets(0, 70, 0, 0);
            c.gridy++;
            add(departamento, c);

            c.insets = new Insets(40, 70, 0, 0);
            c.gridy++;
            add(jButton, c);

            JButton voltar = new JButton("Voltar");
            voltar.addActionListener(new VoltarListener(this));
            c.insets = new Insets(-350, 0, 0, -880);
            add(voltar, c);

            jFrameUtil.configurarJanela(this, Main.getImageIcon(), titulo, 1000, 300);
        }
        catch (DaoException exception){
            dispose();
            Main.getJanelas().add(new Usuarios());
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void carregarInfosUsuario(Integer id) throws DaoException {
        List<Map<String, Object>> infosUsuarioList = daoUtil.select(String.format("SELECT u.login AS login, u.senha AS senha, u.email AS email, u.tentativasAcesso AS tentativasAcesso, u.adm AS adm, u.gerente AS gerente, u.departamento AS deptoId, d.nome as deptoNome FROM USUARIOS u INNER JOIN DEPARTAMENTOS d ON d.id = u.departamento WHERE id = %d", id), Arrays.asList("login", "senha", "email", "tentativasAcesso", "adm", "gerente", "deptoId", "deptoNome"));
        Map<String, Object> infosUsuario = infosUsuarioList.get(0);

        login.setText(infosUsuario.get("login").toString());
        senha.setText(senhaUtil.descriptografar(String.valueOf(infosUsuario.get("senha"))));
        email.setText(infosUsuario.get("email").toString());
        adm.setSelected(Boolean.parseBoolean((String) infosUsuario.get("adm")));
        gerente.setSelected(Boolean.parseBoolean((String) infosUsuario.get("gerente")));
        departamento.setSelectedItem(String.valueOf(infosUsuario.get("deptoNome")));

        jButton.setText("Alterar");

        carregarInfosGrupos(id);
    }

    private void carregarDepartamentos() throws DaoException {
        List<Map<String, Object>> departamentos = daoUtil.select("SELECT nome FROM DEPARTAMENTOS ORDER BY nome", Collections.singletonList("nome"));
        for(Map<String, Object> departamento: departamentos){
            this.departamento.addItem(String.valueOf(departamento.get("nome")));
        }
    }

    private void carregarInfosGrupos(Integer id) throws DaoException {
        gruposIncluidos.removeAllItems();
        gruposExcluidos.removeAllItems();
        gruposIncluidos.addItem(OpcaoComboEnum.GRUPO.getDescricao());
        gruposIncluidos.setSelectedItem(OpcaoComboEnum.GRUPO.getDescricao());
        gruposExcluidos.addItem(OpcaoComboEnum.GRUPO.getDescricao());
        gruposExcluidos.setSelectedItem(OpcaoComboEnum.GRUPO.getDescricao());

        List<Map<String, Object>> grupos = daoUtil.select("SELECT id, nome FROM GRUPOS ORDER BY nome", Arrays.asList("id", "nome"));
        for (Map<String, Object> grupo: grupos) {
            List<Map<String, Object>> participantes = daoUtil.select(String.format("SELECT idusuario FROM USUARIOSGRUPOS WHERE idgrupo = %d", Integer.parseInt(grupo.get("id").toString())), Collections.singletonList("idusuario"));
            if(!participantes.isEmpty()) {
                for (Map<String, Object> participante : participantes) {
                    if (Integer.parseInt(participante.get("idusuario").toString()) == id) {
                        gruposIncluidos.addItem(grupo.get("nome").toString());
                    }
                }
            }
            else {
                gruposExcluidos.addItem(grupo.get("nome").toString());
            }
        }
    }

    public JTextField getLogin() {
        return login;
    }

    public JPasswordField getSenha() {
        return senha;
    }

    public JTextField getEmail() {
        return email;
    }

    public JCheckBox getAdm() {
        return adm;
    }

    public JCheckBox getGerente() {
        return gerente;
    }

    public JComboBox<String> getDepartamento() {
        return departamento;
    }

    public JComboBox<String> getGruposIncluidos() {
        return gruposIncluidos;
    }

    public JComboBox<String> getGruposExcluidos() {
        return gruposExcluidos;
    }
}
