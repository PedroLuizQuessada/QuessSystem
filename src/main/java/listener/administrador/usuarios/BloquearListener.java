package listener.administrador.usuarios;

import controle.DaoUtil;
import exception.DaoException;
import view.administrador.usuarios.Usuarios;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BloquearListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final Usuarios janela;
    private final JButton jButton;
    private final Integer id;

    public BloquearListener(Usuarios janela, JButton jButton, Integer id) {
        this.janela = janela;
        this.jButton = jButton;
        this.id = id;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int tentativasAcesso = 0;
        if(jButton.getText().equals("Bloquear")){
            tentativasAcesso = 3;
        }

        try {
            daoUtil.update(String.format("UPDATE USUARIOS SET tentativasAcesso = %d WHERE id = %d", tentativasAcesso, id));
            janela.carregarUsuarios("SELECT u.id AS id, u.login AS login, u.email AS email, u.tentativasAcesso AS tentativasAcesso, u.adm AS adm, u.gerente AS gerente, u.nativo AS nativo, d.nome AS nomeDepto FROM USUARIOS u INNER JOIN DEPARTAMENTOS d ON u.departamento = d.id ORDER BY login");
            JOptionPane.showMessageDialog(null, "Usuário atualizado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
