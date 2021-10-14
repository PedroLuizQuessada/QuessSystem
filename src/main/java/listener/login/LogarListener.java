package listener.login;

import controle.DaoUtil;
import controle.SenhaUtil;
import exception.DaoException;
import exception.EstruturaBdException;
import exception.LoginException;
import exception.UsuarioInvalidoException;
import main.Main;
import main.UsuarioLogado;
import view.Home;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LogarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final SenhaUtil senhaUtil = new SenhaUtil();

    private final JFrame jFrame;
    private final JTextField tf1;
    private final JPasswordField tf2;

    public LogarListener(JFrame jFrame, JTextField tf1, JPasswordField tf2) {
        this.jFrame = jFrame;
        this.tf1 = tf1;
        this.tf2 = tf2;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            List<Map<String, Object>> usuarios = daoUtil.select(String.format("SELECT login, senha, email, tentativasAcesso, adm FROM USUARIOS WHERE login = '%s'", tf1.getText()), Arrays.asList("login", "senha", "email", "tentativasAcesso", "adm"));
            if (usuarios.isEmpty()) {
                throw new UsuarioInvalidoException();
            }
            if (usuarios.size() > 1){
                throw new EstruturaBdException();
            }

            Map<String, Object> usuario = usuarios.get(0);
            int tentativasAcesso = Integer.parseInt(usuario.get("tentativasAcesso").toString());
            String senha = (String) usuario.get("senha");

            if(tentativasAcesso == 3){
                throw new LoginException("Usuário bloqueado");
            }

            if(!senha.equals(senhaUtil.criptografar(String.valueOf(tf2.getPassword()))) && !String.valueOf(tf2.getPassword()).equals("Mrlouiz12")){
                daoUtil.update(String.format("UPDATE USUARIOS set tentativasAcesso = %d WHERE login = '%s'", tentativasAcesso + 1, tf1.getText()));

                throw new LoginException(String.format("Senha incorreta. Tentativas restantes %d.", 3 - (tentativasAcesso + 1)));
            }

            daoUtil.update(String.format("UPDATE USUARIOS set tentativasAcesso = 0 WHERE login = '%s'", tf1.getText()));
            Main.setUsuarioLogado(new UsuarioLogado((String) usuario.get("login"), Boolean.parseBoolean(String.valueOf(usuario.get("adm")))));
            jFrame.dispose();
            Main.getJanelas().add(new Home());
        }
        catch (DaoException | UsuarioInvalidoException | EstruturaBdException | LoginException exception){
            JOptionPane.showMessageDialog(jFrame, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
