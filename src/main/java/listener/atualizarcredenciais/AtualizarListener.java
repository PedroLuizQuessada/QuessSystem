package listener.atualizarcredenciais;

import controle.DaoUtil;
import controle.SenhaUtil;
import controle.validacoes.UsuarioUtil;
import exception.DaoException;
import exception.validacoes.UsuarioException;
import main.Main;
import view.Home;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AtualizarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final UsuarioUtil usuarioUtil = new UsuarioUtil();
    private final SenhaUtil senhaUtil = new SenhaUtil();

    private final JFrame janela;
    private final JTextField login;
    private final JPasswordField senha;
    private final JTextField email;

    public AtualizarListener(JFrame janela, JTextField login, JPasswordField senha, JTextField email) {
        this.janela = janela;
        this.login = login;
        this.senha = senha;
        this.email = email;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            usuarioUtil.validarUsuario(Main.getUsuarioLogado().getId(), login, senha, email, null);
            daoUtil.update(String.format("UPDATE USUARIOS SET login = '%s', senha = '%s', email = '%s' WHERE id = %d", login.getText(), senhaUtil.criptografar(String.valueOf(senha.getPassword())), email.getText(), Main.getUsuarioLogado().getId()));

            janela.dispose();
            Main.getJanelas().add(new Home());
            JOptionPane.showMessageDialog(null, "Credenciais atualizadas", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (UsuarioException | DaoException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
