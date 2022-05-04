package listener.modelagem.cadastros.campos;

import controle.DaoUtil;
import controle.enums.TipoCampoEnum;
import exception.DaoException;
import view.modelagem.cadastros.campos.Campos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExcluirListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final Campos janela;
    private final Integer idCadastro;
    private final Integer idCampo;
    private final Integer ordemCampo;
    private final Integer agrupadorId;
    private final String tipoCampo;

    public ExcluirListener(Campos janela, Integer idCadastro, Integer idCampo, Integer ordemCampo, Integer agrupadorId, String tipoCampo) {
        this.janela = janela;
        this.idCadastro = idCadastro;
        this.idCampo = idCampo;
        this.ordemCampo = ordemCampo;
        this.agrupadorId = agrupadorId;
        this.tipoCampo = tipoCampo;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if(agrupadorId == null) {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordem = ordem - 1 WHERE ordem > %d AND inativo <> true AND idcadastro = %d", ordemCampo, idCadastro));
            }
            else {
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET ordemagrupador = ordemagrupador - 1 WHERE ordemagrupador > %d AND agrupador = %d AND inativo <> true AND idcadastro = %d", ordemCampo, agrupadorId, idCadastro));
            }

            if(tipoCampo.equalsIgnoreCase(TipoCampoEnum.TEXTO.getDescricao())){
                daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSTEXTO WHERE idcampo = %d", idCampo));
            }
            else if(tipoCampo.equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())){
                daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSNUMERICO WHERE idcampo = %d", idCampo));
            }
            else if(tipoCampo.equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())){
                daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSDATAHORA WHERE idcampo = %d", idCampo));
            }
            else if(tipoCampo.equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())){
                daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSDATA WHERE idcampo = %d", idCampo));
            }
            else if(tipoCampo.equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())){
                daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSCHECKBOX WHERE idcampo = %d", idCampo));
            }
            else if(tipoCampo.equalsIgnoreCase(TipoCampoEnum.AREATEXTO.getDescricao())){
                daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSAREATEXTO WHERE idcampo = %d", idCampo));
            }
            else if(tipoCampo.equalsIgnoreCase(TipoCampoEnum.COMBOBOX.getDescricao())){
                daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSCOMBOBOX WHERE idcampo = %d", idCampo));
            }
            else if(tipoCampo.equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())){
                daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSRADIO WHERE idcampo = %d", idCampo));
            }
            else if(tipoCampo.equalsIgnoreCase(TipoCampoEnum.AGRUPADOR.getDescricao())){
                daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET inativo = true, vinculado = false, pesquisavel = false WHERE idcadastro = %d AND agrupador = %d", idCadastro, idCampo));
                daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSAGRUPADOR WHERE idcampo = %d", idCampo));

                List<Map<String, Object>> idsAgrupados = daoUtil.select(String.format("SELECT id, tipo FROM CAMPOSCADASTROS WHERE idcadastro = %d AND agrupador = %d", idCadastro, idCampo), Arrays.asList("id", "tipo"));
                for (Map<String, Object> idAgrupado: idsAgrupados){
                    if(idAgrupado.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.TEXTO.getDescricao())){
                        daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSTEXTO WHERE idcampo = %d", Integer.parseInt(idAgrupado.get("id").toString())));
                    }
                    else if(idAgrupado.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.NUMERICO.getDescricao())){
                        daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSNUMERICO WHERE idcampo = %d", Integer.parseInt(idAgrupado.get("id").toString())));
                    }
                    else if(idAgrupado.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATAHORA.getDescricao())){
                        daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSDATAHORA WHERE idcampo = %d", Integer.parseInt(idAgrupado.get("id").toString())));
                    }
                    else if(idAgrupado.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.DATA.getDescricao())){
                        daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSDATA WHERE idcampo = %d", Integer.parseInt(idAgrupado.get("id").toString())));
                    }
                    else if(idAgrupado.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.CHECKBOX.getDescricao())){
                        daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSCHECKBOX WHERE idcampo = %d", Integer.parseInt(idAgrupado.get("id").toString())));
                    }
                    else if(idAgrupado.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.AREATEXTO.getDescricao())){
                        daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSAREATEXTO WHERE idcampo = %d", Integer.parseInt(idAgrupado.get("id").toString())));
                    }
                    else if(idAgrupado.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.COMBOBOX.getDescricao())){
                        daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSCOMBOBOX WHERE idcampo = %d", Integer.parseInt(idAgrupado.get("id").toString())));
                    }
                    else if(idAgrupado.get("tipo").toString().equalsIgnoreCase(TipoCampoEnum.RADIO.getDescricao())){
                        daoUtil.delete(String.format("DELETE FROM CONFIGSCAMPOSRADIO WHERE idcampo = %d", Integer.parseInt(idAgrupado.get("id").toString())));
                    }
                }

                daoUtil.drop(String.format("DROP TABLE agrupador_cad_%d", idCampo));
            }

            daoUtil.update(String.format("UPDATE CAMPOSCADASTROS SET inativo = true, vinculado = false, pesquisavel = false WHERE id = %d", idCampo));
            daoUtil.delete(String.format("DELETE FROM REGRASCONDICIONAIS WHERE cadastro = true AND (idcampo = %d OR idcampoinfo = %d)", idCampo, idCampo));
            janela.carregarCampos(String.format("SELECT id, idcadastro, ordem, label, coluna, tipo, nativo, agrupador, ordemagrupador FROM CAMPOSCADASTROS WHERE idcadastro = %d AND inativo <> true ORDER BY ordem, ordemagrupador", idCadastro));
            JOptionPane.showMessageDialog(null, "Campo excluído", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
