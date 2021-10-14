package listener.administrador.departamentos;

import controle.DaoUtil;
import controle.validacoes.DepartamentoUtil;
import exception.DaoException;
import exception.validacoes.DepartamentoException;
import main.Main;
import view.administrador.departamentos.AdicionarConsultar;
import view.administrador.departamentos.Departamentos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CriarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final DepartamentoUtil departamentoUtil = new DepartamentoUtil();

    private final AdicionarConsultar janela;
    private final JTextField nome;
    private final JComboBox<String> gerente;

    public CriarListener(AdicionarConsultar janela) {
        this.janela = janela;
        this.nome = janela.getNome();
        this.gerente = janela.getUsuariosGerente();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            departamentoUtil.validarDepartamento(null, nome);
            daoUtil.insert(String.format("INSERT INTO DEPARTAMENTOS (nome) VALUES ('%s')", nome.getText()));

            List<Map<String, Object>> idDepartamento = daoUtil.select("SELECT MAX(id) AS idDepto FROM DEPARTAMENTOS", Collections.singletonList("idDepto"));
            Integer idDepto = Integer.parseInt(idDepartamento.get(0).get("idDepto").toString());
            daoUtil.update(String.format("UPDATE USUARIOS SET gerente = true, departamento = %d WHERE login = '%s'", idDepto, gerente.getSelectedItem()));

            janela.dispose();
            Main.getJanelas().add(new Departamentos());
            JOptionPane.showMessageDialog(null, "Departamento adicionado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (DepartamentoException | DaoException exception){
            JOptionPane.showMessageDialog(janela, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
