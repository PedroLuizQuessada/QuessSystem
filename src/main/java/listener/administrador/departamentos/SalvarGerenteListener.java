package listener.administrador.departamentos;

import controle.DaoUtil;
import exception.DaoException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SalvarGerenteListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final Integer id;
    private final JComboBox<String> usuariosGerente;

    public SalvarGerenteListener(Integer id, JComboBox<String> usuariosGerente) {
        this.id = id;
        this.usuariosGerente = usuariosGerente;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            daoUtil.update(String.format("UPDATE USUARIOS SET gerente = false WHERE departamento = %d", id));
            daoUtil.update(String.format("UPDATE USUARIOS SET gerente = true WHERE login = '%s'", usuariosGerente.getSelectedItem()));
            JOptionPane.showMessageDialog(null, "Gerente atualizado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
