package view;

import controle.DaoUtil;
import controle.JFrameUtil;
import controle.PermissaoUtil;
import controle.enums.OpcaoHomeEnum;
import exception.DaoException;
import listener.home.AutomacaoListener;
import listener.home.CadastroListener;
import listener.home.SairListener;
import main.Main;
import main.UsuarioLogado;
import view.administrador.automacoes.Automacoes;
import view.administrador.departamentos.Departamentos;
import view.administrador.grupos.Grupos;
import view.administrador.usuarios.Usuarios;
import view.modelagem.cadastros.Cadastros;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Home extends JFrame implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final PermissaoUtil permissaoUtil = new PermissaoUtil();

    private final JMenuBar jMenuBar = new JMenuBar();

    private final JMenu perfilMenu = new JMenu();
    private final JMenu cadastroMenu = new JMenu("Cadastro");
    private final JMenu automacaoMenu = new JMenu("Automação");
    private final JMenu administradorMenu = new JMenu("Administrador");
    private final JMenu modelagemMenu = new JMenu("Modelagem");

    private final JMenuItem atualizarCredenciaisItem = new JMenuItem("Atualizar credenciais");
    private final JMenuItem sairItem = new JMenuItem("Sair");
    private final JMenuItem usuariosItem = new JMenuItem(OpcaoHomeEnum.USUARIOS.getDescricao());
    private final JMenuItem departamentosItem = new JMenuItem(OpcaoHomeEnum.DEPARTAMENTOS.getDescricao());
    private final JMenuItem gruposItem = new JMenuItem(OpcaoHomeEnum.GRUPOS.getDescricao());
    private final JMenuItem automacoesItem = new JMenuItem(OpcaoHomeEnum.AUTOMACOES.getDescricao());
    private final JMenuItem cadastrosItem = new JMenuItem(OpcaoHomeEnum.CADASTROS.getDescricao());

    public Home(){
        carregarUsuarioLogado();

        perfilMenu.setText(Main.getUsuarioLogado().getLogin());
        jMenuBar.add(perfilMenu);

        perfilMenu.add(atualizarCredenciaisItem);
        atualizarCredenciaisItem.addActionListener(this);

        perfilMenu.add(sairItem);
        sairItem.addActionListener(new SairListener(this));

        jMenuBar.add(cadastroMenu);
        carregarCadastros();

        jMenuBar.add(automacaoMenu);
        carregarAutomacoes();

        if(Main.getUsuarioLogado().isAdm()) {
            jMenuBar.add(administradorMenu);
            jMenuBar.add(modelagemMenu);

            administradorMenu.add(usuariosItem);
            usuariosItem.addActionListener(this);

            administradorMenu.add(departamentosItem);
            departamentosItem.addActionListener(this);

            administradorMenu.add(gruposItem);
            gruposItem.addActionListener(this);

            administradorMenu.add(automacoesItem);
            automacoesItem.addActionListener(this);

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

        if(e.getActionCommand().equalsIgnoreCase(OpcaoHomeEnum.ATUALIZARCREDENCIAIS.getDescricao())) {
            Main.getJanelas().add(new AtualizarCredenciais());
        }
        else if(e.getActionCommand().equalsIgnoreCase(OpcaoHomeEnum.USUARIOS.getDescricao())) {
            Main.getJanelas().add(new Usuarios());
        }
        else if(e.getActionCommand().equalsIgnoreCase(OpcaoHomeEnum.DEPARTAMENTOS.getDescricao())) {
            Main.getJanelas().add(new Departamentos());
        }
        else if(e.getActionCommand().equalsIgnoreCase(OpcaoHomeEnum.GRUPOS.getDescricao())) {
            Main.getJanelas().add(new Grupos());
        }
        else if (e.getActionCommand().equalsIgnoreCase(OpcaoHomeEnum.AUTOMACOES.getDescricao())) {
            Main.getJanelas().add(new Automacoes());
        }
        else if(e.getActionCommand().equalsIgnoreCase(OpcaoHomeEnum.CADASTROS.getDescricao())) {
            Main.getJanelas().add(new Cadastros());
        }
    }

    private void carregarCadastros(){
        Map<Integer, JMenuItem> itens = permissaoUtil.carregarItens("CADASTROS");
        for (Map.Entry<Integer, JMenuItem> itemCadastro: itens.entrySet()) {
            itemCadastro.getValue().addActionListener(new CadastroListener(this, itemCadastro.getKey()));
            cadastroMenu.add(itemCadastro.getValue());
        }
    }

    private void carregarAutomacoes(){
        Map<Integer, JMenuItem> itens = permissaoUtil.carregarItens("AUTOMACOES");
        for (Map.Entry<Integer, JMenuItem> itemAutomacao: itens.entrySet()) {
            itemAutomacao.getValue().addActionListener(new AutomacaoListener(this, itemAutomacao.getKey()));
            automacaoMenu.add(itemAutomacao.getValue());
        }
    }

    private void carregarUsuarioLogado() {
        try {
            List<Map<String, Object>> usuarioList = daoUtil.select(String.format("SELECT id, login, senha, email, adm, departamento FROM USUARIOS WHERE id = %d", Main.getUsuarioLogado().getId()), Arrays.asList("id", "login", "senha", "email", "adm", "departamento"));
            Map<String, Object> usuario = usuarioList.get(0);

            Main.setUsuarioLogado(new UsuarioLogado(Integer.parseInt(usuario.get("id").toString()), String.valueOf(usuario.get("login")), String.valueOf(usuario.get("senha")), String.valueOf(usuario.get("email")), Boolean.parseBoolean(String.valueOf(usuario.get("adm"))), Integer.parseInt(usuario.get("departamento").toString())));
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
