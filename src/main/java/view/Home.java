package view;

import controle.DaoUtil;
import controle.JFrameUtil;
import controle.enums.OpcaoHomeEnum;
import controle.enums.TipoPermissaoEnum;
import exception.DaoException;
import listener.home.CadastroListener;
import listener.home.SairListener;
import main.Main;
import view.administrador.departamentos.Departamentos;
import view.administrador.grupos.Grupos;
import view.administrador.usuarios.Usuarios;
import view.modelagem.cadastros.Cadastros;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Home extends JFrame implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final JFrameUtil jFrameUtil = new JFrameUtil();

    private final JMenuBar jMenuBar = new JMenuBar();

    private final JMenu perfilMenu = new JMenu();
    private final JMenu cadastroMenu = new JMenu("Cadastro");
    private final JMenu administradorMenu = new JMenu("Administrador");
    private final JMenu modelagemMenu = new JMenu("Modelagem");

    private final JMenuItem sairItem = new JMenuItem("Sair");
    private final JMenuItem usuariosItem = new JMenuItem(OpcaoHomeEnum.USUARIOS.getDescricao());
    private final JMenuItem departamentosItem = new JMenuItem(OpcaoHomeEnum.DEPARTAMENTOS.getDescricao());
    private final JMenuItem gruposItem = new JMenuItem(OpcaoHomeEnum.GRUPOS.getDescricao());
    private final JMenuItem cadastrosItem = new JMenuItem(OpcaoHomeEnum.CADASTROS.getDescricao());

    public Home(){
        perfilMenu.setText(Main.getUsuarioLogado().getLogin());
        jMenuBar.add(perfilMenu);
        perfilMenu.add(sairItem);
        sairItem.addActionListener(new SairListener(this));

        jMenuBar.add(cadastroMenu);
        carregarCadastros();

        if(Main.getUsuarioLogado().isAdm()) {
            jMenuBar.add(administradorMenu);
            jMenuBar.add(modelagemMenu);

            administradorMenu.add(usuariosItem);
            usuariosItem.addActionListener(this);

            administradorMenu.add(departamentosItem);
            departamentosItem.addActionListener(this);

            administradorMenu.add(gruposItem);
            gruposItem.addActionListener(this);

            modelagemMenu.add(cadastrosItem);
            cadastrosItem.addActionListener(this);
        }

        setJMenuBar(jMenuBar);
        jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Quess System", 850, 300);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (JFrame janela: Main.getJanelas()){
            janela.dispose();
        }

        if(e.getActionCommand().equalsIgnoreCase(OpcaoHomeEnum.USUARIOS.getDescricao())) {
            Main.getJanelas().add(new Usuarios());
        }
        else if(e.getActionCommand().equalsIgnoreCase(OpcaoHomeEnum.DEPARTAMENTOS.getDescricao())) {
            Main.getJanelas().add(new Departamentos());
        }
        else if(e.getActionCommand().equalsIgnoreCase(OpcaoHomeEnum.GRUPOS.getDescricao())) {
            Main.getJanelas().add(new Grupos());
        }
        else if(e.getActionCommand().equalsIgnoreCase(OpcaoHomeEnum.CADASTROS.getDescricao())) {
                Main.getJanelas().add(new Cadastros());
        }
    }

    private void carregarCadastros(){
        try {
            List<Map<String, Object>> cadastros = daoUtil.select(String.format("SELECT id, nome, tipopermissao, permitidos FROM CADASTROS WHERE tipopermissao != '%s'", TipoPermissaoEnum.PADRAO.getDescricao()), Arrays.asList("id", "nome", "tipopermissao", "permitidos"));

            for (Map<String, Object> cadastro : cadastros) {
                if(cadastro.get("tipopermissao").toString().equalsIgnoreCase(TipoPermissaoEnum.USUARIO.getDescricao())){
                    if(Main.getUsuarioLogado().getId() == Integer.parseInt(cadastro.get("permitidos").toString())){
                        JMenuItem itemCadastro = new JMenuItem(cadastro.get("nome").toString());
                        itemCadastro.addActionListener(new CadastroListener(this, Integer.parseInt(cadastro.get("id").toString())));
                        cadastroMenu.add(itemCadastro);
                    }
                }
                else if (cadastro.get("tipopermissao").toString().equalsIgnoreCase(TipoPermissaoEnum.DEPARTAMENTO.getDescricao())){
                    if(Main.getUsuarioLogado().getDepartamento() == Integer.parseInt(cadastro.get("permitidos").toString())){
                        JMenuItem itemCadastro = new JMenuItem(cadastro.get("nome").toString());
                        itemCadastro.addActionListener(new CadastroListener(this, Integer.parseInt(cadastro.get("id").toString())));
                        cadastroMenu.add(itemCadastro);
                    }
                }
                else if (cadastro.get("tipopermissao").toString().equalsIgnoreCase(TipoPermissaoEnum.GRUPO.getDescricao())){
                    List<Map<String, Object>> grupo = daoUtil.select(String.format("SELECT id FROM USUARIOSGRUPOS WHERE idusuario = %d AND idgrupo = %d", Main.getUsuarioLogado().getId(), Integer.parseInt(cadastro.get("permitidos").toString())), Collections.singletonList("id"));
                    if(!grupo.isEmpty()){
                        JMenuItem itemCadastro = new JMenuItem(cadastro.get("nome").toString());
                        itemCadastro.addActionListener(new CadastroListener(this, Integer.parseInt(cadastro.get("id").toString())));
                        cadastroMenu.add(itemCadastro);
                    }
                }
                else if (cadastro.get("tipopermissao").toString().equalsIgnoreCase(TipoPermissaoEnum.GERENTE.getDescricao())){
                    List<Map<String, Object>> gerente = daoUtil.select(String.format("SELECT id FROM USUARIOS WHERE departamento = %d AND gerente = true AND id = %d", Integer.parseInt(cadastro.get("permitidos").toString()), Main.getUsuarioLogado().getId()), Collections.singletonList("id"));
                    if(!gerente.isEmpty()){
                        JMenuItem itemCadastro = new JMenuItem(cadastro.get("nome").toString());
                        itemCadastro.addActionListener(new CadastroListener(this, Integer.parseInt(cadastro.get("id").toString())));
                        cadastroMenu.add(itemCadastro);
                    }
                }
                else if (cadastro.get("tipopermissao").toString().equalsIgnoreCase(TipoPermissaoEnum.LIDER.getDescricao())){
                    List<Map<String, Object>> lider = daoUtil.select(String.format("SELECT id FROM GRUPOS WHERE lider = %d AND id = %d", Main.getUsuarioLogado().getId(), Integer.parseInt(cadastro.get("permitidos").toString())), Collections.singletonList("id"));
                    if(!lider.isEmpty()){
                        JMenuItem itemCadastro = new JMenuItem(cadastro.get("nome").toString());
                        itemCadastro.addActionListener(new CadastroListener(this, Integer.parseInt(cadastro.get("id").toString())));
                        cadastroMenu.add(itemCadastro);
                    }
                }
            }
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
