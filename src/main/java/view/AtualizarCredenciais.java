package view;

import controle.JFrameUtil;
import controle.SenhaUtil;
import listener.atualizarcredenciais.AtualizarListener;
import listener.home.VoltarListener;
import main.Main;

import javax.swing.*;
import java.awt.*;

public class AtualizarCredenciais extends JFrame {
    private final JFrameUtil jFrameUtil = new JFrameUtil();
    private final SenhaUtil senhaUtil = new SenhaUtil();

    public AtualizarCredenciais() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("Login"), c);

        c.insets = new Insets(0, 20, 0, 0);
        c.gridx++;
        JTextField login = new JTextField();
        login.setText(Main.getUsuarioLogado().getLogin());
        login.setColumns(20);
        add(login, c);

        c.insets = new Insets(20, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        add(new JLabel("Senha"), c);

        c.insets = new Insets(20, 20, 0, 0);
        c.gridx++;
        JPasswordField senha = new JPasswordField();
        senha.setText(senhaUtil.descriptografar(Main.getUsuarioLogado().getSenha()));
        senha.setColumns(20);
        add(senha, c);

        c.insets = new Insets(20, 0, 0, 0);
        c.gridx = 0;
        c.gridy++;
        add(new JLabel("E-mail"), c);

        c.insets = new Insets(20, 20, 0, 0);
        c.gridx++;
        JTextField email = new JTextField();
        email.setText(Main.getUsuarioLogado().getEmail());
        email.setColumns(20);
        add(email, c);

        c.insets = new Insets(20, -140, 0, 0);
        c.gridy++;
        JButton atualizar = new JButton("Atualizar");
        atualizar.addActionListener(new AtualizarListener(this, login, senha, email));
        add(atualizar, c);

        c.gridx++;
        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(new VoltarListener(this));
        this.add(voltar, c);

        jFrameUtil.configurarJanela(this, Main.getImageIcon(), "Atualizar credenciais", 850, 300);
    }
}
