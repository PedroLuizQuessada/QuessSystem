package listener.modelagem.cadastros.campos;

import controle.ConfigsUtil;
import controle.DaoUtil;
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

            List<Map<String, Object>> ordemInicialList = daoUtil.select(String.format("SELECT ordem FROM CAMPOSCADASTROS WHERE id = %d", idCampo), Collections.singletonList("ordem"));
            int ordemInicial = Integer.parseInt(ordemInicialList.get(0).get("ordem").toString());
            int fim = Integer.parseInt(ordem.getSelectedItem().toString());

            if(ordemInicial > Integer.parseInt(ordem.getSelectedItem().toString())){
                for(int i = ordemInicial - 1; i >= fim; i--){
                    daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordem = ordem + 1 WHERE ordem = %d AND idcadastro = %d",  i, idCadastro));
                }
            }
            else{
                for(int i = ordemInicial + 1; i <= fim; i++){
                    daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordem = ordem - 1 WHERE ordem = %d AND idcadastro = %d", i, idCadastro));
                }
            }

            daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordem = %d, label = '%s' WHERE id = %d", Integer.parseInt(ordem.getSelectedItem().toString()), label.getText(), idCampo));
            configsUtil.alterarConfigs(idCampo, true);

            janela.dispose();
            Main.getJanelas().add(new Campos(idCadastro));
            JOptionPane.showMessageDialog(null, "Campo alterado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (CampoException | DaoException exception){
            exception.printStackTrace();
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
