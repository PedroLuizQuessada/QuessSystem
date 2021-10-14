package view;

import controle.JFrameUtil;
import listener.login.LogarListener;
import listener.login.LogarSenhaListener;
import listener.login.RecuperarSenhaListener;
import main.Main;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();

    public Login() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Login"), c);

        c.insets = new Insets(0, 20, 0, 0);
        c.gridx++;
        JTextField login = new JTextField();
        login.setColumns(20);
        add(login, c);

        c.insets = new Insets(20, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        add(new JLabel("Senha"), c);

        c.insets = new Insets(20, 20, 0, 0);
        c.gridx++;
        JPasswordField senha = new JPasswordField();
        senha.addKeyListener(new LogarSenhaListener(this, login, senha));
        senha.setColumns(20);
        add(senha, c);

        c.insets = new Insets(20, 0, 0, 0);
        c.gridy++;
        JButton logar = new JButton("Logar");
        logar.addActionListener(new LogarListener(this, login, senha));
        add(logar, c);

        c.gridy++;
        JButton recuperarSenha = new JButton("Recuperar senha");
        recuperarSenha.addActionListener(new RecuperarSenhaListener(login, recuperarSenha, logar));
        add(recuperarSenha, c);

        jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Login", 850, 300);
    }
}
