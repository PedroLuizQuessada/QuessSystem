package listener.login;

import controle.DaoUtil;
import controle.EmailUtil;
import controle.SenhaUtil;
import exception.DaoException;
import exception.EmailException;
import exception.EstruturaBdException;
import exception.UsuarioInvalidoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class RecuperarSenhaListener implements ActionListener, Runnable {
    private final DaoUtil daoUtil = new DaoUtil();
    private final EmailUtil emailUtil = new EmailUtil();
    private final SenhaUtil senhaUtil = new SenhaUtil();

    private final JTextField usuario;
    private final JButton recuperarSenha;
    private final JButton logar;
    private List<Map<String, Object>> emails;

    public RecuperarSenhaListener(JTextField usuario, JButton recuperarSenha, JButton logar) {
        this.usuario = usuario;
        this.recuperarSenha = recuperarSenha;
        this.logar = logar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            emails = daoUtil.select(String.format("SELECT email FROM usuarios WHERE login = '%s'", usuario.getText()), Collections.singletonList("email"));
            if (emails.isEmpty()) {
                throw new UsuarioInvalidoException();
            }
            if (emails.size() > 1){
                throw new EstruturaBdException();
            }

            recuperarSenha.setText("Gerando nova senha...");
            recuperarSenha.setEnabled(false);
            logar.setEnabled(false);
            new Thread(this).start();
        }
        catch (DaoException | UsuarioInvalidoException | EstruturaBdException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void run(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    String novaSenha = emailUtil.enviarSenha(emails.get(0).get("email").toString());
                    String senhaCriptografada = senhaUtil.criptografar(novaSenha);

                    daoUtil.update(String.format("UPDATE usuarios SET senha = '%s' WHERE login = '%s'", senhaCriptografada, usuario.getText()));
                    JOptionPane.showMessageDialog(null, "Nova senha enviada por e-mail", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                }
                catch (DaoException | EmailException exception){
                    JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }

                recuperarSenha.setText("Recuperar senha");
                recuperarSenha.setEnabled(true);
                logar.setEnabled(true);
            }
        });
    }
}
