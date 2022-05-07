package controle.mascaras;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class HoraUtil implements KeyListener {
    private final JTextField campo;

    public HoraUtil(JTextField campo) {
        this.campo = campo;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(campo.getText().length() == 2) {
            campo.setText(campo.getText() + ":");
        }
        else if(campo.getText().length() == 5){
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (campo.getText().length() == 5) {
            ValidadorHora validadorHora = new ValidadorHora();
            if (!validadorHora.isValido(campo.getText())) {
                campo.setText("");
            }
        }
    }

    private static class ValidadorHora {
        private boolean isValido(String horaTexto) {
            String hora = horaTexto.substring(0, 2);
            String minuto = horaTexto.substring(3);

            try {
                if (Integer.parseInt(hora) < 0 || Integer.parseInt(hora) > 23) {
                    return false;
                }

                if (Integer.parseInt(minuto) < 0 || Integer.parseInt(minuto) > 59) {
                    return false;
                }
            }
            catch (Exception exception) {
                return false;
            }

            return true;
        }
    }
}
