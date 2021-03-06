package listener.modelagem.cadastros.campos;

import controle.ConfigsUtil;
import controle.DaoUtil;
import controle.enums.TipoCampoEnum;
import controle.validacoes.CampoUtil;
import exception.DaoException;
import exception.validacoes.CampoException;
import main.Main;
import view.modelagem.cadastros.campos.AdicionarConsultar;
import view.modelagem.cadastros.campos.Campos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AlterarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final CampoUtil campoUtil = new CampoUtil();
    private final ConfigsUtil configsUtil;

    private final AdicionarConsultar janela;
    private final Integer idCadastro;
    private final Integer idCampo;
    private final JComboBox<String> ordem;
    private final JTextField label;

    public AlterarListener(AdicionarConsultar janela, Integer idCadastro, Integer idCampo, ConfigsUtil configsUtil) {
        this.configsUtil = configsUtil;

        this.janela = janela;
        this.idCadastro = idCadastro;
        this.idCampo = idCampo;
        this.ordem = janela.getOrdem();
        this.label = janela.getLabel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            campoUtil.validarCampo(idCadastro, idCampo, label, null, janela.getTipo(), configsUtil);
            String colunaOrdem = "ordem";

            List<Map<String, Object>> ordemInicialList = daoUtil.select(String.format("SELECT ordem, ordemagrupador, agrupador FROM CAMPOSCADASTROS WHERE id = %d", idCampo), Collections.singletonList("ordem"));

            if(ordemInicialList.get(0).get("agrupador") != null){
                colunaOrdem = "ordemagrupador";
            }

            int ordemInicial = Integer.parseInt(ordemInicialList.get(0).get(colunaOrdem).toString());
            int fim = Integer.parseInt(ordem.getSelectedItem().toString());

            if(ordemInicial > Integer.parseInt(ordem.getSelectedItem().toString())){
                for(int i = ordemInicial - 1; i >= fim; i--){
                    if(colunaOrdem.equalsIgnoreCase("ordem")) {
                        daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordem = ordem + 1 WHERE ordem = %d AND idcadastro = %d", i, idCadastro));
                    }
                    else {
                        daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordemagrupador = ordemagrupador + 1 WHERE ordemagrupador = %d AND idcadastro = %d AND agrupador = %d", i, idCadastro, Integer.parseInt(ordemInicialList.get(0).get("agrupador").toString())));
                    }
                }
            }
            else{
                for(int i = ordemInicial + 1; i <= fim; i++){
                    if(colunaOrdem.equalsIgnoreCase("ordem")) {
                        daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordem = ordem - 1 WHERE ordem = %d AND idcadastro = %d", i, idCadastro));
                    }
                    else {
                        daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordemagrupador = ordemagrupador - 1 WHERE ordemagrupador = %d AND idcadastro = %d AND agrupador = %d", i, idCadastro, Integer.parseInt(ordemInicialList.get(0).get("agrupador").toString())));
                    }
                }
            }

            if(janela.getTipo().getSelectedItem().toString().equalsIgnoreCase(TipoCampoEnum.AGRUPADOR.getDescricao())){
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordem = %d WHERE agrupador = %d", fim, idCampo));
            }
            daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordem = %d, label = '%s' WHERE id = %d", Integer.parseInt(ordem.getSelectedItem().toString()), label.getText(), idCampo));
            configsUtil.alterarConfigs(idCampo, true);

            janela.dispose();
            Main.getJanelas().add(new Campos(idCadastro));
            JOptionPane.showMessageDialog(null, "Campo alterado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (CampoException | DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
