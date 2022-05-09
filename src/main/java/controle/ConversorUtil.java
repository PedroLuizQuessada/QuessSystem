package controle;

import exception.ConversaoException;

import java.util.Calendar;

public class ConversorUtil {
    public static String getDateToString(Calendar data) throws ConversaoException {
        try {
            String dia = String.valueOf(data.get(Calendar.DAY_OF_MONTH));
            if (dia.length() == 1) {
                dia = "0" + dia;
            }

            String mes = String.valueOf(data.get(Calendar.MONTH) + 1);
            if (mes.length() == 1) {
                mes = "0" + mes;
            }

            String ano = String.valueOf(data.get(Calendar.YEAR));
            String hora = String.valueOf(data.get(Calendar.HOUR_OF_DAY));
            if (hora.length() == 1) {
                hora = "0" + hora;
            }

            String minuto = String.valueOf(data.get(Calendar.MINUTE));
            if (minuto.length() == 1) {
                minuto = "0" + minuto;
            }

            String segundo = String.valueOf(data.get(Calendar.SECOND));
            if (segundo.length() == 1) {
                segundo = "0" + segundo;
            }

            return String.format("%s %s %s  %s %s %s", dia, mes, ano, hora, minuto, segundo);
        }
        catch (Exception exception) {
            throw new ConversaoException("getDateToString");
        }
    }
}
