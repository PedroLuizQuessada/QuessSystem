package controle.validacoes;

import controle.DaoUtil;
import controle.EmailUtil;
import exception.DaoException;
import exception.EmailException;
import exception.validacoes.AutomacaoException;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AutomacaoUtil {
    private final DaoUtil daoUtil = new DaoUtil();
    private final EmailUtil emailUtil = new EmailUtil();

    public void validarAutomacao(Integer id, JTextField nome, JCheckBox horarioLimites, JTextField horarioLimiteInicio, JTextField horarioLimiteFim, JTextField emailFalha) throws AutomacaoException, DaoException {
        if (nome.getText().length() < 5) {
            throw new AutomacaoException("O nome deve ter pelo menos 5 caracteres");
        }

        try {
            emailUtil.validarEmail(emailFalha.getText());
        }
        catch (EmailException exception) {
            throw new AutomacaoException("E-mail inválido");
        }

        if (horarioLimites.isSelected()) {
            if (Integer.parseInt(horarioLimiteInicio.getText().substring(0, 2)) >= Integer.parseInt(horarioLimiteFim.getText().substring(0, 2))) {
                throw new AutomacaoException("Horário de limite de fim deve ser maior que o horário de limite de ínicio");
            }
            if (Integer.parseInt(horarioLimiteInicio.getText().substring(3)) >= Integer.parseInt(horarioLimiteFim.getText().substring(3))) {
                throw new AutomacaoException("Horário de limite de fim deve ser maior que o horário de limite de ínicio");
            }
        }

        List<Map<String, Object>> automacoes = daoUtil.select(String.format("SELECT COUNT(id) as total FROM AUTOMACOES WHERE nome = '%s' AND id <> %d", nome.getText(), id), Collections.singletonList("total"));
        if(Integer.parseInt(automacoes.get(0).get("total").toString()) > 0){
            throw new AutomacaoException("O nome deve ser único");
        }
    }
}
