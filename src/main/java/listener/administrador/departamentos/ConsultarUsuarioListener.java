package listener.administrador.departamentos;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;
import main.Main;
import view.administrador.departamentos.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ConsultarUsuarioListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final AdicionarConsultar janela;
    private final JComboBox<String> usuariosInclusos;

    public ConsultarUsuarioListener(AdicionarConsultar janela) {
        this.janela = janela;
        this.usuariosInclusos = janela.getUsuariosInclusos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String login = (String) usuariosInclusos.getSelectedItem();
        if(login != null && login.equals(OpcaoComboEnum.USUARIO.getDescricao())){
            JOptionPane.showMessageDialog(null, "Necessário escolher um usuário", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        else {
            try {
                List<Map<String, Object>> usuarioList = daoUtil.select(String.format("SELECT id FROM USUARIOS WHERE login = '%s'", login), Collections.singletonList("id"));
                janela.dispose();
                Main.getJanelas().add(new view.administrador.usuarios.AdicionarConsultar(Integer.parseInt(usuarioList.get(0).get("id").toString())));
            } catch (DaoException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
