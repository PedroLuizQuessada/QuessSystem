package controle.validacoes;

import controle.enums.OpcoesPadraoRegrasCondicionaisEnum;
import exception.validacoes.RegraCondicionalException;

import javax.swing.*;

public class RegraCondicionalUtil {
    private final JComboBox<String> tipoRegra;
    private final JComboBox<String> campo;
    private final JComboBox<String> operador;
    private final JComboBox<String> valor;

    public RegraCondicionalUtil(JComboBox<String> tipoRegra, JComboBox<String> campo, JComboBox<String> operador, JComboBox<String> valor) {
        this.tipoRegra = tipoRegra;
        this.campo = campo;
        this.operador = operador;
        this.valor = valor;
    }

    public void validarRegraCondicional() throws RegraCondicionalException {
        if (tipoRegra.getSelectedItem().equals(OpcoesPadraoRegrasCondicionaisEnum.TIPOREGRA.getDescricao())) {
            throw new RegraCondicionalException("tipo de regra");
        }

        if (campo.getSelectedItem().equals(OpcoesPadraoRegrasCondicionaisEnum.CAMPO.getDescricao())) {
            throw new RegraCondicionalException("campo");
        }

        if (operador.getSelectedItem().equals(OpcoesPadraoRegrasCondicionaisEnum.OPERADOR.getDescricao())) {
            throw new RegraCondicionalException("operador");
        }

        if (valor.getSelectedItem().equals(OpcoesPadraoRegrasCondicionaisEnum.VALOR.getDescricao())) {
            throw new RegraCondicionalException("valor");
        }
    }
}
