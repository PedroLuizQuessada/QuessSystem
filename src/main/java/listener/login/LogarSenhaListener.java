package listener.login;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LogarSenhaListener implements KeyListener {
    private final LogarListener listener;

    public LogarSenhaListener(JFrame janela, JTextField login, JPasswordField senha) {
        this.listener = new LogarListener(janela, login, senha);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            listener.actionPerformed(null);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
