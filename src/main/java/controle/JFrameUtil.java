package controle;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JFrameUtil {
    public void configurarJanela(JFrame janela, ImageIcon icone, String titulo, int largura, int altura){
        janela.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });
        janela.setIconImage(icone.getImage());
        janela.pack();
        janela.setTitle(titulo);
        janela.setVisible(true);
        janela.setResizable(false);
        janela.setSize(largura, altura);
    }
}
