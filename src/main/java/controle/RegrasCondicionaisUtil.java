package controle;

import controle.enums.OperadoresRegrasCondicionaisEnum;
import controle.enums.TipoCampoEnum;
import controle.enums.TiposRegrasRegrasCondicionaisEnum;
import exception.DaoException;

import javax.swing.*;
import java.util.*;

public class RegrasCondicionaisUtil {
    private final DaoUtil daoUtil = new DaoUtil();

    private final Map<String, Object> valoresCampos;
    private final Map<String, Object> campos;

    public RegrasCondicionaisUtil(Map<String, Object> valoresCampos, Map<String, Object> campos) {
        this.valoresCampos = valoresCampos;
        this.campos = campos;
    }

    public boolean verificarRegraCondicional(String nomeCadastro, Integer idCampo, Integer idRegistro, TiposRegrasRegrasCondicionaisEnum tipoRegra) throws DaoException {
        boolean resultado = false;
        List<Map<String, Object>> regrasCondicionais = daoUtil.select(String.format("SELECT rc.operador AS operador, rc.valor AS valor, rc.grupo AS grupo, cc.coluna AS coluna, cc.tipo AS tipo, rc.idcampoinfo AS idcampoinfo FROM regrascondicionais rc INNER JOIN camposcadastros cc ON rc.idcampoinfo = cc.id WHERE rc.cadastro = true AND rc.idcampo = %d AND rc.tiporegra = '%s' ORDER BY rc.grupo", idCampo, tipoRegra.getDescricao()), Arrays.asList("operador", "valor", "grupo", "coluna", "tipo", "idcampoinfo"));

        if (regrasCondicionais.isEmpty()) {
            switch (tipoRegra) {
                case VISIBILIDADE:
                    resultado = true;
                    break;

                case BLOQUEIO:

                case OBRIGATORIEDADE:
                    resultado = false;
                    break;
            }
        } else {
            Map<Integer, Boolean> gruposRegras = new HashMap<>();
            for (Map<String, Object> regraCondicional : regrasCondicionais) {
                String operador = String.valueOf(regraCondicional.get("operador"));
                String valor = String.valueOf(regraCondicional.get("valor"));
                Integer grupo = Integer.parseInt(regraCondicional.get("grupo").toString());
                String tipo = String.valueOf(regraCondicional.get("tipo"));
                Integer idCampoInfo = Integer.parseInt(regraCondicional.get("idcampoinfo").toString());
                String coluna = String.valueOf(regraCondicional.get("coluna"));

                boolean resultadoAnaliseGrupo = gruposRegras.getOrDefault(grupo, true);
                if (!resultadoAnaliseGrupo) {
                    continue;
                }

                if (tipo.equals(TipoCampoEnum.CHECKBOX.getDescricao())) {
                    if (valor.equals("Sim")) {
                        valor = "true";
                    } else if (valor.equals("Não")) {
                        valor = "false";
                    }
                }

                if (tipoRegra == TiposRegrasRegrasCondicionaisEnum.OBRIGATORIEDADE) {
                    if (!campos.containsKey(coluna)) {
                        continue;
                    }

                    String valorPreenchido = "";
                    if (tipo.equals(TipoCampoEnum.CHECKBOX.getDescricao())) {
                        valorPreenchido = String.valueOf(((JCheckBox) campos.get(coluna)).isSelected());
                    }
                    else if (tipo.equals(TipoCampoEnum.COMBOBOX.getDescricao())) {
                        valorPreenchido = String.valueOf(((JComboBox<String>) campos.get(coluna)).getSelectedItem());
                    }
                    else if (tipo.equals(TipoCampoEnum.RADIO.getDescricao())) {
                        valorPreenchido = ((ButtonGroup) campos.get(coluna)).getSelection().getActionCommand();
                    }

                    analisarValorCondicional(operador, valor, valorPreenchido, grupo, gruposRegras);
                }
                else if (valoresCampos == null) {
                    if (idRegistro != null) {
                        List<Map<String, Object>> infoRegraCondicionalList = daoUtil.select(String.format("SELECT %s FROM %s WHERE id = %d", coluna, nomeCadastro, idRegistro), Collections.singletonList(coluna));
                        if (!infoRegraCondicionalList.isEmpty()) {
                            String infoRegraCondicional = String.valueOf(infoRegraCondicionalList.get(0).get(coluna));
                            analisarValorCondicional(operador, valor, infoRegraCondicional, grupo, gruposRegras);
                        }
                    } else {
                        List<Map<String, Object>> infosCampo = daoUtil.select(String.format("SELECT vinculado, tipo FROM CAMPOSCADASTROS WHERE id = %d", idCampoInfo), Arrays.asList("vinculado", "tipo"));
                        if (String.valueOf(infosCampo.get(0).get("vinculado")).equalsIgnoreCase("true")) {
                            String tipoCampo = String.valueOf(infosCampo.get(0).get("tipo"));
                            String estadoPadrao = "";
                            if (tipoCampo.equals(TipoCampoEnum.CHECKBOX.getDescricao())) {
                                List<Map<String, Object>> estadoPadraoList = daoUtil.select(String.format("SELECT estadopadrao FROM CONFIGSCAMPOSCHECKBOX WHERE idcampo = %d AND cadastro = true", idCampoInfo), Collections.singletonList("estadopadrao"));
                                estadoPadrao = String.valueOf(estadoPadraoList.get(0).get("estadopadrao"));
                            }
                            if (tipoCampo.equals(TipoCampoEnum.COMBOBOX.getDescricao())) {
                                List<Map<String, Object>> estadoPadraoList = daoUtil.select(String.format("SELECT estadopadrao FROM CONFIGSCAMPOSCOMBOBOX WHERE idcampo = %d AND cadastro = true", idCampoInfo), Collections.singletonList("estadopadrao"));
                                estadoPadrao = String.valueOf(estadoPadraoList.get(0).get("estadopadrao"));
                            }
                            if (tipoCampo.equals(TipoCampoEnum.RADIO.getDescricao())) {
                                List<Map<String, Object>> estadoPadraoList = daoUtil.select(String.format("SELECT estadopadrao FROM CONFIGSCAMPOSRADIO WHERE idcampo = %d AND cadastro = true", idCampoInfo), Collections.singletonList("estadopadrao"));
                                estadoPadrao = String.valueOf(estadoPadraoList.get(0).get("estadopadrao"));
                            }

                            analisarValorCondicional(operador, valor, estadoPadrao, grupo, gruposRegras);
                        } else {
                            switch (tipoRegra) {
                                case VISIBILIDADE:
                                    gruposRegras.put(grupo, true);
                                    break;

                                case BLOQUEIO:
                                    gruposRegras.put(grupo, false);
                                    break;
                            }
                        }
                    }
                }
                else {
                    String valorFormularioRecarregado = String.valueOf(valoresCampos.get(coluna));
                    analisarValorCondicional(operador, valor, valorFormularioRecarregado, grupo, gruposRegras);
                }
            }

            for (Map.Entry<Integer, Boolean> grupoRegra : gruposRegras.entrySet()) {
                if (grupoRegra.getValue()) {
                    resultado = true;
                    break;
                }
            }
        }

        return resultado;
    }

    private void analisarValorCondicional(String operador, String valorEsperado, String valorEncontrado, Integer grupo, Map<Integer, Boolean> gruposRegras) {
        if (operador.equals(OperadoresRegrasCondicionaisEnum.IGUAL.getDescricao())) {
            if (!valorEsperado.equals(valorEncontrado)) {
                gruposRegras.put(grupo, false);
            } else {
                gruposRegras.put(grupo, true);
            }
        } else if (operador.equals(OperadoresRegrasCondicionaisEnum.DIFERENTE.getDescricao())) {
            if (valorEsperado.equals(valorEncontrado)) {
                gruposRegras.put(grupo, false);
            } else {
                gruposRegras.put(grupo, true);
            }
        }
    }
}
