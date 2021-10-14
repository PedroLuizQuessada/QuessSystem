package listener.home;

import main.Main;
import view.Login;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SairListener implements ActionListener {
    private final JFrame jFrame;

    public SairListener(JFrame jFrame) {
        this.jFrame = jFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for(JFrame janela: Main.getJanelas()){
            janela.dispose();
        }

        Main.setUsuarioLogado(null);
        jFrame.dispose();
        new Login();
    }
}
