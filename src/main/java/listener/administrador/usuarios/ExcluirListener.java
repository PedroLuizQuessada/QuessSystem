package listener.administrador.usuarios;

import controle.DaoUtil;
import exception.DaoException;
import view.administrador.usuarios.Usuarios;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExcluirListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final Usuarios janela;
    private final Integer id;

    public ExcluirListener(Usuarios janela, Integer id) {
        this.janela = janela;
        this.id = id;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            daoUtil.delete(String.format("DELETE FROM USUARIOS WHERE id = %d", id));
            daoUtil.update(String.format("UPDATE CADASTROS SET permitidos = -1 WHERE tipopermissao = 'Usuário' AND permitidos = %d", id));
            janela.carregarUsuarios("SELECT u.id AS id, u.login AS login, u.email AS email, u.tentativasAcesso AS tentativasAcesso, u.adm AS adm, u.gerente AS gerente, u.nativo AS nativo, d.nome AS nomeDepto FROM USUARIOS u INNER JOIN DEPARTAMENTOS d ON u.departamento = d.id ORDER BY login");
            JOptionPane.showMessageDialog(null, "Usuário excluído", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
