package controle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AreaTextoUtil implements KeyListener {
    private final JTextArea campo;
    private int limiteCaracteres;

    public AreaTextoUtil(JTextArea campo, int... limiteCaracteres){
        this.campo = campo;
        if(limiteCaracteres.length > 0) {
            this.limiteCaracteres = limiteCaracteres[0];
        }
    }

    public void configurarTextArea(){
        campo.setPreferredSize(new Dimension(300, 65));
        campo.setLineWrap(true);
        campo.setWrapStyleWord(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (campo.getText().length() >= limiteCaracteres){
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
