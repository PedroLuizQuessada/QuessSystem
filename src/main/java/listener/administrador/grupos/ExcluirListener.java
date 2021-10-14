package listener.administrador.grupos;

import controle.DaoUtil;
import exception.DaoException;
import view.administrador.grupos.Grupos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExcluirListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final Grupos janela;
    private final Integer id;

    public ExcluirListener(Grupos janela, Integer id) {
        this.janela = janela;
        this.id = id;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            daoUtil.delete(String.format("DELETE FROM GRUPOS WHERE id = %d", id));
            daoUtil.delete(String.format("DELETE FROM USUARIOSGRUPOS WHERE idgrupo = %d", id));
            daoUtil.update(String.format("UPDATE CADASTROS SET permitidos = -1 WHERE (tipopermissao = 'Grupos' OR tipopermissao = 'Líder de grupo') AND permitidos = %d", id));
            janela.carregarGrupos("SELECT g.id AS id, g.nome AS nome, u.login AS lider FROM GRUPOS g LEFT JOIN USUARIOS u ON u.id = g.lider ORDER BY g.nome");
            JOptionPane.showMessageDialog(null, "Grupo excluído", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
