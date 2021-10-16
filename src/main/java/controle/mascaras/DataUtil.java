package controle.mascaras;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DataUtil implements KeyListener {
    private final JTextField campo;

    public DataUtil(JTextField campo) {
        this.campo = campo;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(campo.getText().length() == 2 || campo.getText().length() == 5){
            campo.setText(campo.getText() + "/");
        }
        else if(!Character.isDigit(e.getKeyChar()) || campo.getText().length() == 10){
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
