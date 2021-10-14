package listener.administrador.grupos;

import controle.DaoUtil;
import controle.validacoes.GrupoUtil;
import exception.DaoException;
import exception.validacoes.GrupoException;
import main.Main;
import view.administrador.grupos.Grupos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlterarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final GrupoUtil grupoUtil = new GrupoUtil();

    private final JFrame janela;
    private final int id;
    private final JTextField nome;

    public AlterarListener(JFrame janela, Integer id, JTextField nome){
        this.janela = janela;
        this.id = id;
        this.nome = nome;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            grupoUtil.validarGrupo(id, nome);
            daoUtil.update(String.format("UPDATE GRUPOS SET nome = '%s' WHERE id = %d", nome.getText(), id));
            janela.dispose();
            Main.getJanelas().add(new Grupos());
            JOptionPane.showMessageDialog(null, "Grupos atualizado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (GrupoException | DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
