package listener.administrador.departamentos;

import controle.DaoUtil;
import exception.DaoException;
import view.administrador.departamentos.Departamentos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ExcluirListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final Departamentos janela;
    private final int id;

    public ExcluirListener(Departamentos janela, int id) {
        this.janela = janela;
        this.id = id;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            List<Map<String, Object>> usuarios = daoUtil.select(String.format("SELECT COUNT(1) AS usuarios FROM USUARIOS WHERE departamento = %d", id), Collections.singletonList("usuarios"));
            if(Integer.parseInt(usuarios.get(0).get("usuarios").toString()) == 0) {
                daoUtil.delete(String.format("DELETE FROM DEPARTAMENTOS WHERE id = '%d'", id));
                daoUtil.update(String.format("UPDATE CADASTROS SET permitidos = -1 WHERE (tipopermissao = 'Departamento' OR tipopermissao = 'Gerente de departamento') AND permitidos = %d", id));
                janela.carregarDepartamentos("SELECT d.id AS deptoId, d.nome AS deptoNome, u.login AS usuLogin FROM DEPARTAMENTOS d LEFT JOIN USUARIOS u ON u.departamento = d.id WHERE u.gerente = true ORDER BY d.nome");
                JOptionPane.showMessageDialog(null, "Departamento excluído", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(null, "Existem usuário que pertencem a esse departamento. Necessário realoca-los antes realizar a exclusão.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
        catch (DaoException exception){
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
