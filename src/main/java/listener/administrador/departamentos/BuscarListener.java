package listener.administrador.departamentos;

import view.administrador.departamentos.Departamentos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuscarListener implements ActionListener {
    private final Departamentos janela;
    private final JTextField nome;
    private final JTextField gerente;

    public BuscarListener(Departamentos janela) {
        this.janela = janela;
        this.nome = janela.getNomeFiltro();
        this.gerente = janela.getGerenteFiltro();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String join = "LEFT";
        if(gerente.getText().length() > 0){
            join = "INNER";
        }
        janela.carregarDepartamentos("SELECT d.id AS deptoId, d.nome AS deptoNome, u.login AS usuLogin FROM DEPARTAMENTOS d " + join + " JOIN USUARIOS u ON u.departamento = d.id WHERE u.gerente = true AND d.nome LIKE '%" + nome.getText() + "%' AND u.login LIKE '%" + gerente.getText() + "%' ORDER BY d.nome");
    }
}
