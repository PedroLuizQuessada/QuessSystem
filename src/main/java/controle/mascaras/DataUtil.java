package controle.mascaras;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DataUtil implements KeyListener {
    private final JTextField campo;

    public DataUtil(JTextField campo) {
        this.campo = campo;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (campo.getText().length() == 2 || campo.getText().length() == 5) {
            campo.setText(campo.getText() + "/");
        } else if (!Character.isDigit(e.getKeyChar()) || campo.getText().length() == 10) {
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (campo.getText().length() == 10) {
            ValidadorData data = new ValidadorData();
            if (!data.isValido(campo.getText())) {
                campo.setText("");
            }
        }
    }

    public String converterDataSql() {
        String data = "";

        if (campo.getText().length() == 10) {
            data = data + campo.getText().substring(6) + "-";
            data = data + campo.getText().substring(3, 5) + "-";
            data = data + campo.getText().substring(0, 2);
        } else {
            campo.setText("");
        }

        return data;
    }

    public String converterDataString() {
        String data = "";

        if (campo.getText().length() == 10) {
            data = data + campo.getText().substring(8) + "/";
            data = data + campo.getText().substring(5, 7) + "/";
            data = data + campo.getText().substring(0, 4);
        } else {
            campo.setText("");
        }

        return data;
    }

    private static class ValidadorData {
        private final String formato = "dd/MM/yyyy";

        private boolean isValido(String dateStr) {
            DateFormat dateFormat = new SimpleDateFormat(this.formato);
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(dateStr);
            } catch (ParseException e) {
                return false;
            }
            return true;
        }
    }
}
