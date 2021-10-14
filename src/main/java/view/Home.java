package view;

import controle.JFrameUtil;
import controle.enums.OpcaoHomeEnum;
import listener.home.SairListener;
import main.Main;
import view.administrador.departamentos.Departamentos;
import view.administrador.grupos.Grupos;
import view.administrador.usuarios.Usuarios;
import view.modelagem.cadastros.Cadastros;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Home extends JFrame implements ActionListener {
    private final JFrameUtil jFrameUtil = new JFrameUtil();

    private final JMenuBar jMenuBar = new JMenuBar();

    private final JMenu perfilMenu = new JMenu();
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
}
