package listener.administrador.grupos;

import view.administrador.grupos.Grupos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuscarListener implements ActionListener {
    private final Grupos janela;
    private final JTextField nome;
    private final JTextField lider;

    public BuscarListener(Grupos janela) {
        this.janela = janela;
        this.nome = janela.getNomeFiltro();
        this.lider = janela.getLiderFiltro();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String join = "LEFT";
        if(lider.getText().length() > 0){
            join = "INNER";
        }
        janela.carregarGrupos("SELECT g.id AS id, g.nome AS nome, u.login AS lider FROM GRUPOS g " + join + " JOIN USUARIOS u ON u.id = g.lider WHERE g.nome LIKE '%" + nome.getText() + "%' AND u.login LIKE '%" + lider.getText() + "%' ORDER BY g.nome");
    }
}
