package view.administrador.departamentos;

import controle.DaoUtil;
import controle.JFrameUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;
import listener.administrador.departamentos.*;
import listener.home.VoltarListener;
import main.Main;
import view.administrador.usuarios.Usuarios;

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
    private final JComboBox<String> usuariosGerente = new JComboBox<>();
    private final JButton jButton = new JButton("Criar departamento");

    public AdicionarConsultar(Integer id){
        String titulo = "Adicionar Departamento";
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
            carregarUsuariosGerente(id);

            if(id != null){
                titulo = "Consultar Departamento";

                c.insets = new Insets(30, 0, 0, 50);
                c.gridx = 0;
                c.gridy++;
                add(new JLabel("Participantes"), c);

                c.gridx = 1;
                add(new JLabel("Usuários fora do departamento"), c);

                c.insets = new Insets(30, 0, 0, 0);
                c.gridx = 2;
                add(new JLabel("Gerente"), c);

                c.insets = new Insets(0, 0, 0, 50);
                c.gridx = 0;
                c.gridy++;
                add(usuariosInclusos, c);

                c.gridx = 1;
                add(usuariosExclusos, c);

                c.insets = new Insets(0, 0, 0, 0);
                c.gridx = 2;
                add(usuariosGerente, c);

                c.insets = new Insets(10, 0, 0, 50);
                c.gridx = 0;
                c.gridy++;
                JButton consultarUsuario = new JButton("Consultar usuário");
                consultarUsuario.addActionListener(new ConsultarUsuarioListener(this));
                add(consultarUsuario, c);

                c.gridx = 1;
                JButton adicionarUsuario = new JButton("Adicionar usuário");
                adicionarUsuario.addActionListener(new AdicionarUsuarioListener(this, id));
                add(adicionarUsuario, c);

                c.insets = new Insets(10, 0, 0, 0);
                c.gridx = 2;
                JButton salvarGerente = new JButton("Salvar gerente");
                salvarGerente.addActionListener(new SalvarGerenteListener(id, usuariosGerente));
                add(salvarGerente, c);

                carregarInfosDepartamento(id);
                jButton.addActionListener(new AlterarListener(this, id, nome));

                c.gridx = 1;
                topGap = -350;
            }
            else{
                jButton.addActionListener(new CriarListener(this));

                c.insets = new Insets(0, 0, 0, 0);
                c.gridx = 3;
                c.gridy = 0;
                add(new JLabel("Gerente"), c);

                c.gridy++;
                add(usuariosGerente, c);

                c.gridx = 2;
            }

            c.insets = new Insets(40, 0, 0, 50);
            c.gridy++;
            add(jButton, c);

            c.insets = new Insets(topGap, 0, 0, -650);
            JButton voltar = new JButton("Voltar");
            voltar.addActionListener(new VoltarListener(this));
            add(voltar, c);

            jFrameUtil.configurarJanela(this, Main.getImageIcon(), titulo, 850, 300);
        }
        catch (DaoException exception){
            dispose();
            Main.getJanelas().add(new Usuarios());
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void carregarInfosDepartamento(Integer idDepartamento) throws DaoException {
        usuariosInclusos.removeAllItems();
        usuariosExclusos.removeAllItems();

        List<Map<String, Object>> infosDepartamentoList = daoUtil.select(String.format("SELECT * FROM DEPARTAMENTOS WHERE id = %d", idDepartamento), Collections.singletonList("nome"));
        nome.setText(infosDepartamentoList.get(0).get("nome").toString());

        List<Map<String, Object>> usuarios = daoUtil.select("SELECT departamento, login FROM USUARIOS ORDER BY login", Arrays.asList("departamento", "login"));

        int contInclusos = 0;
        int contExclusos = 0;
        for (Map<String, Object> usuario: usuarios){
            if(Integer.parseInt(usuario.get("departamento").toString()) == idDepartamento){
                usuariosInclusos.insertItemAt(String.valueOf(usuario.get("login").toString()), contInclusos);
                contInclusos++;
            }
            else {
                usuariosExclusos.insertItemAt(String.valueOf(usuario.get("login")), contExclusos);
                contExclusos++;
            }
        }

        usuariosInclusos.insertItemAt(OpcaoComboEnum.USUARIO.getDescricao(), 0);
        usuariosInclusos.setSelectedItem(OpcaoComboEnum.USUARIO.getDescricao());
        usuariosExclusos.insertItemAt(OpcaoComboEnum.USUARIO.getDescricao(), 0);
        usuariosExclusos.setSelectedItem(OpcaoComboEnum.USUARIO.getDescricao());

        jButton.setText("Alterar");
    }

    private void carregarUsuariosGerente(Integer idDepartamento) throws DaoException{
        usuariosGerente.addItem(OpcaoComboEnum.SEM_GERENTE.getDescricao());
        usuariosGerente.setSelectedItem(OpcaoComboEnum.SEM_GERENTE.getDescricao());

        List<Map<String, Object>> usuarios = daoUtil.select("SELECT login, departamento, gerente FROM USUARIOS ORDER BY login", Arrays.asList("login", "departamento", "gerente"));
        for (Map<String, Object> usuario: usuarios){
            usuariosGerente.addItem(String.valueOf(usuario.get("login")));

            if(Boolean.parseBoolean(usuario.get("gerente").toString()) && idDepartamento != null && Integer.parseInt(usuario.get("departamento").toString()) == idDepartamento){
                usuariosGerente.setSelectedItem(String.valueOf(usuario.get("login")));
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

    public JComboBox<String> getUsuariosGerente() {
        return usuariosGerente;
    }
}
