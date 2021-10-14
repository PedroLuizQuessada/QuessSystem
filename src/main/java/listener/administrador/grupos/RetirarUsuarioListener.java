package listener.administrador.grupos;

import controle.DaoUtil;
import controle.enums.OpcaoComboEnum;
import exception.DaoException;
import view.administrador.grupos.AdicionarConsultar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RetirarUsuarioListener implements ActionListener {
    private final DaoUtil daoUtil = new DaoUtil();

    private final AdicionarConsultar janela;
    private final Integer idGrupo;
    private final JComboBox<String> usuariosInclusos;

    public RetirarUsuarioListener(AdicionarConsultar janela, Integer idGrupo) {
        this.janela = janela;
        this.idGrupo = idGrupo;
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
                List<Map<String, Object>> usuarioIdlist = daoUtil.select(String.format("SELECT u.id AS id, g.lider AS lider FROM USUARIOS u LEFT JOIN GRUPOS g ON g.lider = u.id WHERE u.login = '%s'", usuariosInclusos.getSelectedItem()), Arrays.asList("id", "lider"));
                Integer usuarioId = Integer.parseInt(usuarioIdlist.get(0).get("id").toString());
                daoUtil.delete(String.format("DELETE FROM USUARIOSGRUPOS WHERE idusuario = %d AND idgrupo = %d", usuarioId, idGrupo));

                if(!String.valueOf(usuarioIdlist.get(0).get("lider")).equalsIgnoreCase("null")){
                    daoUtil.update(String.format("UPDATE GRUPOS SET lider = null WHERE id = %d", idGrupo));
                }

                janela.carregarInfosGrupo(idGrupo);
                JOptionPane.showMessageDialog(null, "Usuário retirado do grupo", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (DaoException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
