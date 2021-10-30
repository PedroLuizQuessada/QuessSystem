package listener.modelagem.cadastros.campos;

import controle.ComboboxUtil;
import controle.DaoUtil;
import controle.enums.AcoesCampoCadastroEnum;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RealizarAcaoListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final ComboboxUtil comboboxUtil = new ComboboxUtil();

    private final int idCampo;
    private final JButton realizarAcao;
    private final JComboBox<String> acoes;
    private final JComboBox<String> tipo;
    private final JComboBox<String> agrupador;

    public RealizarAcaoListener(Integer idCampo, JButton realizarAcao, JComboBox<String> acoes, JComboBox<String> tipo, JComboBox<String> agrupador) {
        this.idCampo = idCampo;
        this.realizarAcao = realizarAcao;
        this.acoes = acoes;
        this.tipo = tipo;
        this.agrupador = agrupador;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String mensagem = "";
        try {
            if (realizarAcao.getText().equals(AcoesCampoCadastroEnum.VINCULAR.getDescricao())) {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET vinculado = true WHERE id = %d", idCampo));

                if(agrupador != null && !agrupador.getSelectedItem().toString().equalsIgnoreCase(OpcaoComboEnum.SEM_AGRUPADOR.getDescricao())){
                    List<Map<String, Object>> idCadastroList = daoUtil.select(String.format("SELECT idcadastro FROM CAMPOSCADASTROS WHERE id = %d", idCampo), Collections.singletonList("idcadastro"));
                    List<Map<String, Object>> agrupadorList = daoUtil.select(String.format("SELECT id FROM CAMPOSCADASTROS WHERE label = '%s' AND idcadastro = %d", agrupador.getSelectedItem(), Integer.parseInt(idCadastroList.get(0).get("idcadastro").toString())), Collections.singletonList("id"));

                    daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET vinculado = true WHERE id = %d", Integer.parseInt(agrupadorList.get(0).get("id").toString())));
                }

                mensagem = "Campo vinculado";
            }
            if (realizarAcao.getText().equals(AcoesCampoCadastroEnum.DESVINCULAR.getDescricao())) {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET vinculado = false WHERE id = %d", idCampo));
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET vinculado = false WHERE agrupador = %d", idCampo));
                mensagem = "Campo desvinculado";
            }
            if (realizarAcao.getText().equals(AcoesCampoCadastroEnum.BLOQUEAR.getDescricao())) {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET bloqueado = true WHERE id = %d", idCampo));
                mensagem = "Campo bloqueado";
            }
            if (realizarAcao.getText().equals(AcoesCampoCadastroEnum.DESBLOQUEAR.getDescricao())) {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET bloqueado = false WHERE id = %d", idCampo));
                mensagem = "Campo desbloqueado";
            }
            if (realizarAcao.getText().equals(AcoesCampoCadastroEnum.TORNAR_OBRIGATORIO.getDescricao())) {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET obrigatorio = true WHERE id = %d", idCampo));
                mensagem = "Campo tornado obrigatório";
            }
            if (realizarAcao.getText().equals(AcoesCampoCadastroEnum.TORNAR_NAO_OBRIGATORIO.getDescricao())) {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET obrigatorio = false WHERE id = %d", idCampo));
                mensagem = "Campo tornado não obrigatório";
            }
            if (realizarAcao.getText().equals(AcoesCampoCadastroEnum.TORNAR_PESQUISAVEL.getDescricao())) {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET pesquisavel = true WHERE id = %d", idCampo));
                mensagem = "Campo tornado pesquisável";
            }
            if (realizarAcao.getText().equals(AcoesCampoCadastroEnum.TORNAR_NAO_PESQUISAVEL.getDescricao())) {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET pesquisavel = false WHERE id = %d", idCampo));
                mensagem = "Campo tornado não pesquisável";
            }
            if (realizarAcao.getText().equals(AcoesCampoCadastroEnum.QUEBRAR_LINHA.getDescricao())) {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET quebralinha = true WHERE id = %d", idCampo));
                mensagem = "Quebra de linha adicionada no campo";
            }
            if (realizarAcao.getText().equals(AcoesCampoCadastroEnum.NAO_QUEBRAR_LINHA.getDescricao())) {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET quebralinha = false WHERE id = %d", idCampo));
                mensagem = "Quebra de linha retirada no campo";
            }

            comboboxUtil.carregarAcoesCampo(acoes, idCampo, true);
            realizarAcao.setText(AcoesCampoCadastroEnum.PADRAO.getDescricao());
            JOptionPane.showMessageDialog(null, mensagem, "Erro", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception){
            exception.printStackTrace();
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
