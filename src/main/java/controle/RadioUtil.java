package controle;

import exception.DaoException;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RadioUtil {
    private final DaoUtil daoUtil = new DaoUtil();

    public List<JRadioButton> carregarOpcoesRadio(Integer idCampo, ButtonGroup radio) throws DaoException {
        List<JRadioButton> botoes = new ArrayList<>();
        List<Map<String, Object>> opcoesList = daoUtil.select(String.format("SELECT opcoes FROM CONFIGSCAMPOSRADIO WHERE idcampo = %d", idCampo), Collections.singletonList("opcoes"));

        if(opcoesList.get(0).get("opcoes") != null) {
            String[] opcoes = opcoesList.get(0).get("opcoes").toString().split("_");

            for (String opcao: opcoes){
                JRadioButton jRadioButton = new JRadioButton(opcao);
                radio.add(jRadioButton);
                botoes.add(jRadioButton);
            }
        }

        return botoes;
    }
}
