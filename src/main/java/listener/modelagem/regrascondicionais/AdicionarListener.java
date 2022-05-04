package listener.modelagem.regrascondicionais;

import controle.DaoUtil;
import controle.validacoes.RegraCondicionalUtil;
import exception.DaoException;
import exception.validacoes.RegraCondicionalException;
import main.Main;
import view.modelagem.RegrasCondicionais;
import view.modelagem.cadastros.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AdicionarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final RegraCondicionalUtil regraCondicionalUtil;

    private final RegrasCondicionais regrasCondicionais;
    private final Integer idCampo;
    private final Integer idFormulario;
    private final JComboBox<String> tipoRegra;
    private final JComboBox<String> campoInfo;
    private final JComboBox<String> operador;
    private final JComboBox<String> valor;
    private final JComboBox<String> grupo;
    private final boolean cadastro;

    public AdicionarListener(RegrasCondicionais regrasCondicionais, Integer idCampo, Integer idFormulario, JComboBox<String> tipoRegra, JComboBox<String> campoInfo, JComboBox<String> operador, JComboBox<String> valor, JComboBox<String> grupo, boolean cadastro) {
        this.regrasCondicionais = regrasCondicionais;
        this.idCampo = idCampo;
        this.idFormulario = idFormulario;
        this.tipoRegra = tipoRegra;
        this.campoInfo = campoInfo;
        this.operador = operador;
        this.valor = valor;
        this.grupo = grupo;
        this.cadastro = cadastro;

        this.regraCondicionalUtil = new RegraCondicionalUtil(tipoRegra, campoInfo, operador, valor);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            regraCondicionalUtil.validarRegraCondicional();

            List<Map<String, Object>> idCampoList = new ArrayList<>();
            if (cadastro) {
                idCampoList = daoUtil.select(String.format("SELECT id FROM camposcadastros WHERE label = '%s'", campoInfo.getSelectedItem()), Collections.singletonList("id"));
            }

            if (!idCampoList.isEmpty()) {
                int idCampoInfo = Integer.parseInt(idCampoList.get(0).get("id").toString());
                int grupoNum = Integer.parseInt(grupo.getSelectedItem().toString());
                daoUtil.insert(String.format("INSERT INTO regrascondicionais (cadastro, idcampo, idcampoinfo, operador, valor, grupo, tiporegra) VALUES (%s, %d, %d, '%s', '%s', %d, '%s')", cadastro, idCampo, idCampoInfo, operador.getSelectedItem(), valor.getSelectedItem(), grupoNum, tipoRegra.getSelectedItem()));

                regrasCondicionais.dispose();
                Main.getJanelas().add(new RegrasCondicionais(idCampo, idFormulario, cadastro));
                JOptionPane.showMessageDialog(null, "Regra adicionada", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null, "Falha ao criar regra", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (DaoException | RegraCondicionalException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
