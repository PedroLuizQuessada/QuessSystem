package listener.modelagem.regrascondicionais;

import controle.DaoUtil;
import controle.validacoes.RegraCondicionalUtil;
import exception.DaoException;
import exception.validacoes.RegraCondicionalException;
import main.Main;
import view.modelagem.RegrasCondicionais;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AtualizarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final RegraCondicionalUtil regraCondicionalUtil;

    private final RegrasCondicionais regrasCondicionais;
    private final Integer idRegra;
    private final Integer idCampo;
    private final Integer idFormulario;
    private final boolean cadastro;
    private final JComboBox<String> tipoRegra;
    private final JComboBox<String> campoInfo;
    private final JComboBox<String> operador;
    private final JComboBox<String> valor;
    private final JComboBox<String> grupo;

    public AtualizarListener(RegrasCondicionais regrasCondicionais, Integer idRegra, Integer idCampo, Integer idFormulario, boolean cadastro, JComboBox<String> tipoRegra, JComboBox<String> campoInfo, JComboBox<String> operador, JComboBox<String> valor, JComboBox<String> grupo) {
        this.regrasCondicionais = regrasCondicionais;
        this.idRegra = idRegra;
        this.idCampo = idCampo;
        this.idFormulario = idFormulario;
        this.cadastro = cadastro;
        this.tipoRegra = tipoRegra;
        this.campoInfo = campoInfo;
        this.operador = operador;
        this.valor = valor;
        this.grupo = grupo;

        regraCondicionalUtil = new RegraCondicionalUtil(tipoRegra, campoInfo, operador, valor, grupo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            regraCondicionalUtil.validarRegraCondicional();

            List<Map<String, Object>> idCampoInfoList = new ArrayList<>();
            if (cadastro) {
                idCampoInfoList = daoUtil.select(String.format("SELECT id FROM CAMPOSCADASTROS WHERE label = '%s'", campoInfo.getSelectedItem()), Collections.singletonList("id"));
            }

            if (!idCampoInfoList.isEmpty()) {
                int idCampoInfo = Integer.parseInt(idCampoInfoList.get(0).get("id").toString());
                int grupoNum = Integer.parseInt(grupo.getSelectedItem().toString());
                daoUtil.update(String.format("UPDATE REGRASCONDICIONAIS SET tiporegra = '%s', idcampoinfo = %d, operador = '%s', valor = '%s', grupo = %d WHERE id = %d", tipoRegra.getSelectedItem(), idCampoInfo, operador.getSelectedItem(), valor.getSelectedItem(), grupoNum, idRegra));

                regrasCondicionais.dispose();
                Main.getJanelas().add(new RegrasCondicionais(idCampo, idFormulario, cadastro));
                JOptionPane.showMessageDialog(null, "Regra atualizada", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null, "Falha ao atualizar regra", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (DaoException | RegraCondicionalException exception) {
            JOptionPane.showMessageDialog(regrasCondicionais, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
