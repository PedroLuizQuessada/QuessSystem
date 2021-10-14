package listener.administrador.usuarios;

import controle.DaoUtil;
import controle.SenhaUtil;
import controle.validacoes.UsuarioUtil;
import exception.validacoes.UsuarioException;
import exception.DaoException;
import main.Main;
import view.administrador.usuarios.AdicionarConsultar;
import view.administrador.usuarios.Usuarios;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AlterarListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();
    private final UsuarioUtil usuarioUtil = new UsuarioUtil();
    private final SenhaUtil senhaUtil = new SenhaUtil();

    private final AdicionarConsultar janela;
    private final Integer id;
    private final JTextField login;
    private final JPasswordField senha;
    private final JTextField email;
    private final JCheckBox adm;
    private final JCheckBox gerente;
    private final JComboBox<String> departamento;

    public AlterarListener(AdicionarConsultar janela, Integer id) {
        this.janela = janela;
        this.id = id;
        this.login = janela.getLogin();
        this.senha = janela.getSenha();
        this.email = janela.getEmail();
        this.adm = janela.getAdm();
        this.gerente = janela.getGerente();
        this.departamento = janela.getDepartamento();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            usuarioUtil.validarUsuario(id, login, senha, email, departamento);

            List<Map<String, Object>> departamentos = daoUtil.select(String.format("SELECT id FROM DEPARTAMENTOS WHERE nome = '%s'", departamento.getSelectedItem()), Collections.singletonList("id"));
            Integer idDepartamento = Integer.parseInt(departamentos.get(0).get("id").toString());

            if(gerente.isSelected()) {
                daoUtil.update(String.format("UPDATE USUARIOS SET gerente = false WHERE departamento = %d", idDepartamento));
            }

            daoUtil.update(String.format("UPDATE USUARIOS SET login = '%s', senha = '%s', email = '%s', adm = %s, gerente = %s, departamento = %d WHERE id = %d", login.getText(), senhaUtil.criptografar(String.valueOf(senha.getPassword())), email.getText(), adm.isSelected(), gerente.isSelected(), idDepartamento, id));
            janela.dispose();
            Main.getJanelas().add(new Usuarios());
            JOptionPane.showMessageDialog(null, "Usuário alterado", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (UsuarioException | DaoException exception){
            JOptionPane.showMessageDialog(janela, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
