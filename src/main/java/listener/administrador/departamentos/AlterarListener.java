package listener.administrador.departamentos;

import controle.DaoUtil;
import controle.validacoes.DepartamentoUtil;
import exception.DaoException;
import exception.validacoes.DepartamentoException;
import main.Main;
import view.administrador.departamentos.Departamentos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlterarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final DepartamentoUtil departamentoUtil = new DepartamentoUtil();

    private final JFrame janela;
    private final int id;
    private final JTextField nome;

    public AlterarListener(JFrame janela, int id, JTextField nome) {
        this.janela = janela;
        this.id = id;
        this.nome = nome;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            departamentoUtil.validarDepartamento(id, nome);
            daoUtil.update(String.format("UPDATE DEPARTAMENTOS SET nome = '%s' WHERE id = %d", nome.getText(), id));
            janela.dispose();
            Main.getJanelas().add(new Departamentos());
            JOptionPane.showMessageDialog(null, "Departamento atualizado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DepartamentoException | DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
