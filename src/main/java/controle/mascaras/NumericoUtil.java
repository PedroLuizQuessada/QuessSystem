package controle.mascaras;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class NumericoUtil implements KeyListener {
    private final JTextField campo;

    public NumericoUtil(JTextField campo) {
        this.campo = campo;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(campo.getText().length() == 0 && !Character.isDigit(e.getKeyChar()) && e.getKeyChar() != '-'){
            e.consume();
        }
        else if(!Character.isDigit(e.getKeyChar())){
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
