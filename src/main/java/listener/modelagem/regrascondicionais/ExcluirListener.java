package listener.modelagem.regrascondicionais;

import controle.DaoUtil;
import exception.DaoException;
import main.Main;
import view.modelagem.RegrasCondicionais;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExcluirListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final RegrasCondicionais regrasCondicionais;
    private final Integer idCampo;
    private final Integer idFormulario;
    private final Integer idRegraCondicional;
    private final boolean cadastro;

    public ExcluirListener(RegrasCondicionais regrasCondicionais, Integer idCampo, Integer idFormulario, Integer idRegraCondicional, boolean cadastro) {
        this.regrasCondicionais = regrasCondicionais;
        this.idCampo = idCampo;
        this.idFormulario = idFormulario;
        this.idRegraCondicional = idRegraCondicional;
        this.cadastro = cadastro;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            daoUtil.delete(String.format("DELETE FROM regrascondicionais WHERE id = %d", idRegraCondicional));

            regrasCondicionais.dispose();
            Main.getJanelas().add(new RegrasCondicionais(idCampo, idFormulario, cadastro));
            JOptionPane.showMessageDialog(null, "Regra excluída", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception) {
            JOptionPane.showMessageDialog(regrasCondicionais, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
