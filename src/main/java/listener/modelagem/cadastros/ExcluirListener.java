package listener.modelagem.cadastros;

import controle.DaoUtil;
import controle.enums.TipoCampoEnum;
import exception.DaoException;
import view.modelagem.cadastros.Cadastros;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExcluirListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final Cadastros janela;
    private final Integer id;
    private final String tabela;

    public ExcluirListener(Cadastros janela, Integer id, String tabela) {
        this.janela = janela;
        this.id = id;
        this.tabela = tabela;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            List<Map<String, Object>> camposCadastro = daoUtil.select(String.format("SELECT id, tipo FROM CAMPOSCADASTROS WHERE idcadastro = %d", id), Arrays.asList("id", "tipo"));
            for (Map<String, Object> campoCadastro: camposCadastro){
                if(campoCadastro.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.TEXTO.getDescricao())){
                    daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSTEXTO WHERE idcampo = %d", Integer.parseInt(campoCadastro.get("id").toString())));
                }
                else if(campoCadastro.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())){
                    daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSNUMERICO WHERE idcampo = %d", Integer.parseInt(campoCadastro.get("id").toString())));
                }
                else if(campoCadastro.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())){
                    daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSDATAHORA WHERE idcampo = %d", Integer.parseInt(campoCadastro.get("id").toString())));
                }
                else if(campoCadastro.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())){
                    daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSDATA WHERE idcampo = %d", Integer.parseInt(campoCadastro.get("id").toString())));
                }
                else if(campoCadastro.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())){
                    daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSCHECKBOX WHERE idcampo = %d", Integer.parseInt(campoCadastro.get("id").toString())));
                }
                else if(campoCadastro.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.AREATEXTO.getDescricao())){
                    daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSAREATEXTO WHERE idcampo = %d", Integer.parseInt(campoCadastro.get("id").toString())));
                }
                else if(campoCadastro.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.COMBOBOX.getDescricao())){
                    daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSCOMBOBOX WHERE idcampo = %d", Integer.parseInt(campoCadastro.get("id").toString())));
                }
                else if(campoCadastro.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())){
                    daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSRADIO WHERE idcampo = %d", Integer.parseInt(campoCadastro.get("id").toString())));
                }
                else if(campoCadastro.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.AGRUPADOR.getDescricao())){
                    daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSAGRUPADOR WHERE idcampo = %d", Integer.parseInt(campoCadastro.get("id").toString())));
                    daoUtil.drop(String.format("DROP TABLE agrupador_cad_%d", Integer.parseInt(campoCadastro.get("id").toString())));
                }

                daoUtil.delete(String.format("DELETE FROM REGRASCONDICIONAIS WHERE cadastro = true AND (idcampo = %d OR idcampoinfo = %d)", Integer.parseInt(campoCadastro.get("id").toString()), Integer.parseInt(campoCadastro.get("id").toString())));
            }

            daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET inativo = true WHERE idcadastro = %d", id));
            daoUtil.delete(String.format("DELETE FROM CADASTROS WHERE id = %d", id));
            daoUtil.drop(String.format("DROP TABLE %s", tabela));
            janela.carregarCadastros("SELECT id, nome, tabela, tipopermissao, permitidos FROM CADASTROS ORDER BY nome");
            JOptionPane.showMessageDialog(null, "Cadastro excluído", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
