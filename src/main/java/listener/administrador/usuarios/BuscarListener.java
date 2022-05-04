package listener.administrador.usuarios;

import view.administrador.usuarios.Usuarios;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BuscarListener implements ActionListener {
    private final Usuarios janela;
    private final JTextField login;
    private final JTextField email;
    private final JTextField departamento;
    private final JCheckBox gerente;
    private final JCheckBox adm;

    public BuscarListener(Usuarios janela) {
        this.janela = janela;
        this.login = janela.getLoginFiltro();
        this.email = janela.getEmailFiltro();
        this.departamento = janela.getDepartamentoFiltro();
        this.gerente = janela.getGerenteFiltro();
        this.adm = janela.getAdmFiltro();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        janela.carregarUsuarios("SELECT u.id AS id, u.login AS login, u.email AS email, u.tentativasAcesso AS tentativasAcesso, u.adm AS adm, u.gerente AS gerente, u.nativo AS nativo, d.nome AS nomeDepto FROM USUARIOS u INNER JOIN DEPARTAMENTOS d ON u.departamento = d.id WHERE u.login LIKE '%" + login.getText() + "%' AND u.email LIKE '%" + email.getText() + "%' AND u.adm = " + adm.isSelected() + " AND d.nome LIKE '%" + departamento.getText() + "%' AND u.gerente = " + gerente.isSelected() + " ORDER BY u.login");
    }
}
