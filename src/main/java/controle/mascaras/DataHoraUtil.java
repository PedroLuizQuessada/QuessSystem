package controle.mascaras;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DataHoraUtil implements KeyListener {
    private final JTextField campo;

    public DataHoraUtil(JTextField campo) {
        this.campo = campo;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(campo.getText().length() == 2 || campo.getText().length() == 5){
            campo.setText(campo.getText() + "/");
        }
        else if(campo.getText().length() == 10){
            campo.setText(campo.getText() + " ");
        }
        else if(campo.getText().length() == 13){
            campo.setText(campo.getText() + ":");
        }
        else if(!Character.isDigit(e.getKeyChar()) || campo.getText().length() == 16){
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(campo.getText().length() == 16){
            ValidadorDataHora dataHora = new ValidadorDataHora();
            if (!dataHora.isValido(campo.getText())) {
                campo.setText("");
            }
        }
    }

    public String converterDataHoraSql(){
        String dataHora = "";

        if(campo.getText().length() == 16){
            dataHora = dataHora + campo.getText().substring(6, 10) + "-";
            dataHora = dataHora + campo.getText().substring(3, 5) + "-";
            dataHora = dataHora + campo.getText().substring(0, 2);
            dataHora = dataHora + campo.getText().substring(10);
            dataHora = dataHora + ":00";
        }
        else {
            campo.setText("");
        }

        return dataHora;
    }

    public String converterDataHoraString(){
        String dataHora = "";

        if(campo.getText().length() == 16 || campo.getText().length() == 21){
            dataHora = dataHora + campo.getText().substring(8, 10) + "/";
            dataHora = dataHora + campo.getText().substring(5, 7) + "/";
            dataHora = dataHora + campo.getText().substring(0, 4);
            dataHora = dataHora + campo.getText().substring(10, 16);
        }
        else {
            campo.setText("");
        }
        return dataHora;
    }

    private static class ValidadorDataHora {
        private final String formato = "dd/MM/yyyy HH:mm";

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
