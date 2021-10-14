package view.administrador.grupos;

import controle.DaoUtil;
import controle.JFrameUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;
import listener.administrador.grupos.*;
import listener.home.VoltarListener;
import main.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AdicionarConsultar extends JFrame {
    private final DaoUtil daoUtil = new DaoUtil();
    private final JFrameUtil jFrameUtil = new JFrameUtil();

    private final JTextField nome = new JTextField();
    private final JComboBox<String> usuariosInclusos = new JComboBox<>();
    private final JComboBox<String> usuariosExclusos = new JComboBox<>();
    private final JComboBox<String> usuariosLider = new JComboBox<>();
    private final JButton jButton = new JButton("Criar grupo");
    private List<Map<String, Object>> usuarios;

    public AdicionarConsultar(Integer id){
        String titulo = "Adicionar grupo";
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        int topGap = -260;

        c.insets = new Insets(0, 0, 0, 50);
        c.gridx = 1;
        c.gridy = 0;
        add(new JLabel("Nome"), c);

        c.gridy++;
        nome.setColumns(7);
        add(nome, c);

        try{
            carregarUsuarioLider(id);

            if(id != null){
                titulo = "Consultar Grupo";
                c.insets = new Insets(30, 0, 0, 50);
                c.gridx = 0;
                c.gridy++;
                add(new JLabel("Participantes"), c);

                c.gridx = 1;
                add(new JLabel("Usuários fora do grupo"), c);

                c.insets = new Insets(30, 0, 0, 0);
                c.gridx = 2;
                add(new JLabel("Líder"), c);

                c.insets = new Insets(0, 0, 0, 50);
                c.gridx = 0;
                c.gridy++;
                add(usuariosInclusos, c);

                c.gridx = 1;
                add(usuariosExclusos, c);

                c.insets = new Insets(0, 0, 0, 0);
                c.gridx = 2;
                add(usuariosLider, c);

                JButton retirarUsuario = new JButton("Retirar usuário");
                retirarUsuario.addActionListener(new RetirarUsuarioListener(this, id));
                c.insets = new Insets(10, 0, 0, 50);
                c.gridx = 0;
                c.gridy++;
                add(retirarUsuario, c);

                JButton adicionarUsuario = new JButton("Adicionar usuário");
                adicionarUsuario.addActionListener(new AdicionarUsuarioListener(this, id));
                c.gridx = 1;
                add(adicionarUsuario, c);

                JButton salvarLider = new JButton("Salvar Líder");
                salvarLider.addActionListener(new SalvarLiderListener(this, id, usuariosLider));
                c.insets = new Insets(10, 0, 0, 0);
                c.gridx = 2;
                add(salvarLider, c);

                carregarInfosGrupo(id);
                jButton.addActionListener(new AlterarListener(this, id, nome));

                c.gridx = 1;
                topGap = -350;
            }
            else {
                jButton.addActionListener(new CriarListener(this));

                c.insets = new Insets(0, 0, 0, 0);
                c.gridx = 3;
                c.gridy = 0;
                add(new JLabel("Líder"), c);

                c.gridy++;
                add(usuariosLider, c);

                c.gridx = 2;
            }

            c.insets = new Insets(40, 0, 0, 50);
            c.gridy++;
            add(jButton, c);

            JButton voltar = new JButton("Voltar");
            voltar.addActionListener(new VoltarListener(this));
            c.insets = new Insets(topGap, 0, 0, -650);
            add(voltar, c);

            jFrameUtil.configurarJanela(this, Main.getImageIcon(), titulo, 850, 300);
        }
        catch (DaoException exception){
            dispose();
            Main.getJanelas().add(new Grupos());
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void carregarInfosGrupo(Integer idGrupo) throws DaoException{
        usuariosInclusos.removeAllItems();
        usuariosExclusos.removeAllItems();

        List<Map<String, Object>> infosGrupoList = daoUtil.select(String.format("SELECT * FROM GRUPOS WHERE id = %d", idGrupo), Collections.singletonList("nome"));
        nome.setText(infosGrupoList.get(0).get("nome").toString());

        for(Map<String, Object> usuario: usuarios){
            List<Map<String, Object>> usuarioGrupo = daoUtil.select(String.format("SELECT COUNT(1) AS participa FROM USUARIOSGRUPOS WHERE idusuario = %d AND idgrupo = %d", Integer.parseInt(usuario.get("id").toString()), idGrupo), Collections.singletonList("participa"));
            if(Integer.parseInt(usuarioGrupo.get(0).get("participa").toString()) == 1){
                usuariosInclusos.addItem(String.valueOf(usuario.get("login")));
            }
            else {
                usuariosExclusos.addItem(String.valueOf(usuario.get("login")));
            }
        }

        usuariosInclusos.insertItemAt(OpcaoComboEnum.USUARIO.getDescricao(), 0);
        usuariosInclusos.setSelectedItem(OpcaoComboEnum.USUARIO.getDescricao());
        usuariosExclusos.insertItemAt(OpcaoComboEnum.USUARIO.getDescricao(), 0);
        usuariosExclusos.setSelectedItem(OpcaoComboEnum.USUARIO.getDescricao());
        carregarUsuarioLider(idGrupo);
        jButton.setText("Alterar");
    }

    private void carregarUsuarioLider(Integer idGrupo) throws DaoException{
        usuariosLider.removeAllItems();
        usuariosLider.addItem(OpcaoComboEnum.SEM_LIDER.getDescricao());
        usuariosLider.setSelectedItem(OpcaoComboEnum.SEM_LIDER.getDescricao());

        List<Map<String, Object>> lider = daoUtil.select(String.format("SELECT lider FROM GRUPOS WHERE id = %d", idGrupo), Collections.singletonList("lider"));
        int idLider;
        try {
            idLider = Integer.parseInt(lider.get(0).get("lider").toString());
        }
        catch (Exception exception){
            //novo grupo sendo criado
            idLider = -1;
        }

        usuarios = daoUtil.select("SELECT id, login FROM USUARIOS ORDER BY login", Arrays.asList("id", "login"));
        for(Map<String, Object> usuario: usuarios){
            usuariosLider.addItem(String.valueOf(usuario.get("login")));

            if (Integer.parseInt(usuario.get("id").toString()) == idLider){
                usuariosLider.setSelectedItem(String.valueOf(usuario.get("login")));
            }
        }
    }

    public JTextField getNome() {
        return nome;
    }

    public JComboBox<String> getUsuariosInclusos() {
        return usuariosInclusos;
    }

    public JComboBox<String> getUsuariosExclusos() {
        return usuariosExclusos;
    }

    public JComboBox<String> getUsuariosLider() {
        return usuariosLider;
    }
}
